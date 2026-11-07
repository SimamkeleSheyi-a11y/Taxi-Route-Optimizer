import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
// Main Swing window for the app. Handles the dark theme and visual layout.
public class TaxiRouteGUI extends JFrame {

    private final Graph graph;
    private final RouteFinder routeFinder;
    private JComboBox<String> startBox;
    private JComboBox<String> destinationBox;
    private JTextArea resultArea;
    private MapPanel mapPanel;
    private JLabel statusLabel;
    private JLabel distanceLabel;

    // Dark theme color palette
    private final Color background = new Color(10, 19, 34);
    private final Color panelColor = new Color(17, 32, 53);
    private final Color accent = new Color(35, 135, 245);
    private final Color muted = new Color(160, 183, 211);
    private final Color green = new Color(42, 210, 137);

    public TaxiRouteGUI(Graph graph, RouteFinder routeFinder) {
        this.graph = graph;
        this.routeFinder = routeFinder;

        // Standard window setup
        setTitle("Taxi Route Optimizer | Smart Mobility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 740);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);

        buildInterface();
        setVisible(true);
    }

    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(background);
        setContentPane(root);

        // Top header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(14, 29, 49));
        header.setBorder(new EmptyBorder(18, 25, 18, 25));

        JLabel brand = new JLabel("🚕  TAXI ROUTE OPTIMIZER");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 23));

        JLabel tagline = new JLabel("SMART ROUTES  •  BETTER JOURNEYS");
        tagline.setForeground(muted);
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 11));

        
       JButton exitButton = new JButton("Exit  ✕");
       exitButton.setForeground(Color.WHITE);
       exitButton.setBackground(new Color(170, 55, 70));
       exitButton.setFont(new Font("SansSerif", Font.BOLD, 12));
       exitButton.setFocusPainted(false);
       exitButton.setBorderPainted(false);
       exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

       exitButton.addActionListener(event -> {
    // Quick confirmation so users don't accidentally close the app
    int choice = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to exit Taxi Route Optimizer?",
        "Confirm Exit",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            AppLogger.info("User exited the GUI.");
         dispose();
    }
  });

JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
headerRight.setOpaque(false);
headerRight.add(tagline);
headerRight.add(exitButton);

header.add(brand, BorderLayout.WEST);
header.add(headerRight, BorderLayout.EAST);
root.add(header, BorderLayout.NORTH);


        // Main two-column workspace
        JPanel workspace = new JPanel(new GridBagLayout());
        workspace.setBackground(background);
        workspace.setBorder(new EmptyBorder(18, 18, 18, 18));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 12, 14);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        // Left column
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.add(createPlannerCard());
        left.add(Box.createVerticalStrut(14));
        left.add(createLocationsCard());
        left.add(Box.createVerticalGlue());

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.37;
        c.weighty = 1;
        c.gridheight = 2;
        workspace.add(left, c);

        // Right column: visual map
        mapPanel = new MapPanel();
        mapPanel.setPreferredSize(new Dimension(600, 390));
        mapPanel.setMinimumSize(new Dimension(400, 300));

        JPanel mapCard = createCard();
        mapCard.setLayout(new BorderLayout(0, 10));
        mapCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel mapTitle = sectionTitle("ROUTE NETWORK");
        mapCard.add(mapTitle, BorderLayout.NORTH);
        mapCard.add(mapPanel, BorderLayout.CENTER);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.63;
        c.weighty = 0.72; // Map gets more vertical space than results
        c.gridheight = 1;
        c.insets = new Insets(0, 0, 12, 0);
        workspace.add(mapCard, c);

        // Right column: route result
        JPanel resultCard = createCard();
        resultCard.setLayout(new BorderLayout(0, 8));
        resultCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel resultHeader = new JPanel(new BorderLayout());
        resultHeader.setOpaque(false);

        JLabel resultTitle = sectionTitle("YOUR JOURNEY");
        statusLabel = new JLabel("READY");
        statusLabel.setForeground(green);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 11));

        resultHeader.add(resultTitle, BorderLayout.WEST);
        resultHeader.add(statusLabel, BorderLayout.EAST);

        resultArea = new JTextArea(
            "Choose your starting location and destination, then calculate your route."
        );
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setOpaque(false); // Let the card background show through
        resultArea.setForeground(Color.WHITE);
        resultArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        resultArea.setRows(7);

        distanceLabel = new JLabel("— km");
        distanceLabel.setForeground(green);
        distanceLabel.setFont(new Font("SansSerif", Font.BOLD, 27));

        JLabel distanceCaption = new JLabel("TOTAL DISTANCE");
        distanceCaption.setForeground(muted);
        distanceCaption.setFont(new Font("SansSerif", Font.BOLD, 10));

        JPanel distancePanel = new JPanel();
        distancePanel.setOpaque(false);
        distancePanel.setLayout(new BoxLayout(distancePanel, BoxLayout.Y_AXIS));
        distancePanel.add(distanceCaption);
        distancePanel.add(Box.createVerticalStrut(4));
        distancePanel.add(distanceLabel);

        resultCard.add(resultHeader, BorderLayout.NORTH);
        resultCard.add(resultArea, BorderLayout.CENTER);
        resultCard.add(distancePanel, BorderLayout.SOUTH);

        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.38;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        workspace.add(resultCard, c);

        root.add(workspace, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
            "  ●  SYSTEM ONLINE     |     SHORTEST-PATH ENGINE: DIJKSTRA"
        );
        footer.setOpaque(true);
        footer.setBackground(new Color(14, 29, 49));
        footer.setForeground(muted);
        footer.setFont(new Font("Monospaced", Font.PLAIN, 11));
        footer.setBorder(new EmptyBorder(10, 15, 10, 15));
        root.add(footer, BorderLayout.SOUTH);
    }

    private JPanel createPlannerCard() {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Plan your trip");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 21));

        JLabel description = new JLabel(
            "<html><div style='width:240px'>Find the shortest available path through the road network.</div></html>"
        );
        description.setForeground(muted);
        description.setFont(new Font("SansSerif", Font.PLAIN, 12));

        startBox = new JComboBox<>();
        destinationBox = new JComboBox<>();
        
        for (Location location : graph.getLocations()) {
            startBox.addItem(location.getName());
            destinationBox.addItem(location.getName());
        }
            // Allow users to type or select a location.
            startBox.setEditable(true);
            destinationBox.setEditable(true);

        if (destinationBox.getItemCount() > 1) {
            destinationBox.setSelectedIndex(3 < destinationBox.getItemCount()
                ? 3 : 1);
        }

        styleCombo(startBox);
        styleCombo(destinationBox);

        JButton findButton = new JButton("  Find Shortest Route  →");
        findButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        findButton.setForeground(Color.WHITE);
        findButton.setBackground(accent);
        findButton.setFocusPainted(false);
        findButton.setBorderPainted(false);
        findButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        findButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        findButton.addActionListener(event -> findRoute());

        JButton swapButton = new JButton("⇅  Swap locations");
        swapButton.setForeground(muted);
        swapButton.setBackground(panelColor);
        swapButton.setFocusPainted(false);
        swapButton.setBorderPainted(false);
        swapButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        swapButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        swapButton.addActionListener(event -> {
        Object first = startBox.getSelectedItem();
        Object second = destinationBox.getSelectedItem();
        startBox.setSelectedItem(second);
        destinationBox.setSelectedItem(first);
        });

        card.add(title);
        card.add(Box.createVerticalStrut(7));
        card.add(description);
        card.add(Box.createVerticalStrut(23));
        card.add(fieldLabel("●  STARTING LOCATION"));
        card.add(Box.createVerticalStrut(7));
        card.add(startBox);
        card.add(Box.createVerticalStrut(17));
        card.add(fieldLabel("●  DESTINATION"));
        card.add(Box.createVerticalStrut(7));
        card.add(destinationBox);
        card.add(Box.createVerticalStrut(20));
        card.add(findButton);
        card.add(Box.createVerticalStrut(8));
        card.add(swapButton);

        return card;
    }
    // Builds the left-bottom card that just lists all locations in the graph
    private JPanel createLocationsCard() {
        JPanel card = createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 18, 15, 18));

        JLabel title = sectionTitle("AVAILABLE LOCATIONS");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(10));

        for (Location location : graph.getLocations()) {
            JLabel item = new JLabel("  ●   " + location.getName());
            item.setForeground(new Color(207, 222, 240));
            item.setFont(new Font("SansSerif", Font.PLAIN, 13));
            item.setBorder(new EmptyBorder(7, 0, 7, 0));
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(item);
        }

        return card;
    }
    // Helper to create a standard panel with a subtle border
    private JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(37, 58, 84)),
            new EmptyBorder(1, 1, 1, 1)
        ));
        return panel;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(181, 205, 235));
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(151, 187, 226));
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    // Standardize the look of the dropdowns
    private void styleCombo(JComboBox<String> combo) {

        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setForeground(Color.WHITE);
        combo.setBackground(new Color(31, 49, 73));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

   
private void findRoute() {
    Object startValue = startBox.getSelectedItem();
    Object endValue = destinationBox.getSelectedItem();

    String startName = startValue == null
        ? "" : startValue.toString().trim();

    String endName = endValue == null
        ? "" : endValue.toString().trim();

    // Check that both fields contain text.
    if (startName.isEmpty() || endName.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Please enter both a starting location and a destination.",
            "Missing location",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // Check that both locations exist in the graph.
    Location start = graph.findLocation(startName);
    Location end = graph.findLocation(endName);

    if (start == null || end == null) {
        // Build a dynamic error message showing exactly what was wrong
        StringBuilder message = new StringBuilder(
            "The following location were not found:\n"
        );

        if (start == null) {
            message.append("- Starting location: ")
                   .append(startName).append("\n");
        }

        if (end == null) {
            message.append("- Destination: ")
                   .append(endName).append("\n");
        }

        message.append(
            "\nAvailable locations: Bellville, Parow, "
            + "Goodwood, Cape Town, Atlantis."
        );

        JOptionPane.showMessageDialog(
            this,
            message.toString(),
            "Location not found",
            JOptionPane.ERROR_MESSAGE
        );

         // Reset the UI state to show failure
        statusLabel.setText("INVALID LOCATION");
        statusLabel.setForeground(new Color(255, 105, 120));
        distanceLabel.setText("— km");
        resultArea.setText(
            "Please enter a valid location from the available list."
        );
        mapPanel.setRoute(null);
        return;
    }

    try {
        //Run the algorithm
        Route route = routeFinder.bestRoute(start, end);
        mapPanel.setRoute(route);// Update the map visualization

        if (route == null) {
            AppLogger.warning(
                "GUI: No route found from " + startName
                + " to " + endName
            );

            statusLabel.setText("NO ROUTE");
            statusLabel.setForeground(new Color(255, 105, 120));
            distanceLabel.setText("— km");

            resultArea.setText(
                "No route exists between " + startName
                + " and " + endName
                + ". Try another destination."
            );
        } else {
            // Success! Update labels and text area
            AppLogger.info("GUI: Route calculated: " + route);

            statusLabel.setText("✓ ROUTE FOUND");
            statusLabel.setForeground(green);

            distanceLabel.setText(
                String.format("%.2f km", route.getTotalDistance())
            );

            resultArea.setText(
                "SHORTEST PATH\n"
                + route + "\n\n"
                + "Stops on route: " + route.getPath().size()
                + "\nThe displayed distance uses sample road data."
            );
        }
    } catch (IllegalArgumentException exception) {
        JOptionPane.showMessageDialog(
            this,
            exception.getMessage(),
            "Route error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}


    // A stylized network diagram, not a geographic map.
    private class MapPanel extends JPanel {
        private Route currentRoute;

        MapPanel() {
            setBackground(new Color(8, 24, 41));
        }

        void setRoute(Route route) {
            currentRoute = route;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Subtle background grid
            g.setColor(new Color(19, 43, 63));
            for (int x = 0; x < w; x += 28) {
                g.drawLine(x, 0, x, h);
            }
            for (int y = 0; y < h; y += 28) {
                g.drawLine(0, y, w, y);
            }

            String[] names = {
                "Bellville", "Parow", "Goodwood", "Cape Town", "Atlantis"
            };

            double[][] positions = {
                {0.79, 0.30},
                {0.52, 0.43},
                {0.38, 0.68},
                {0.17, 0.84},
                {0.48, 0.12}
            };

            int[][] roads = {
                {0, 1}, {0, 2}, {1, 2}, {1, 3}, {2, 3}
            };

            // Draw all available connections first.
            g.setStroke(new BasicStroke(2f));
            g.setColor(new Color(60, 87, 115));

            for (int[] road : roads) {
                drawRoad(g, positions, road[0], road[1], w, h);
            }

            // Highlight the calculated shortest route.
            if (currentRoute != null) {
                List<Location> path = currentRoute.getPath();
                g.setStroke(new BasicStroke(4f,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.setColor(new Color(36, 164, 255));

                for (int i = 0; i < path.size() - 1; i++) {
                    int a = indexOf(names, path.get(i).getName());
                    int b = indexOf(names, path.get(i + 1).getName());

                    if (a >= 0 && b >= 0) {
                        drawRoad(g, positions, a, b, w, h);
                    }
                }
            }

            Color[] nodeColors = {
                new Color(47, 215, 143),
                new Color(49, 157, 255),
                new Color(255, 181, 49),
                new Color(255, 91, 120),
                new Color(172, 151, 255)
            };

            for (int i = 0; i < names.length; i++) {
                int x = (int) (positions[i][0] * w);
                int y = (int) (positions[i][1] * h);

                g.setColor(new Color(0, 0, 0, 70));
                g.fillOval(x - 11, y - 9, 24, 24);

                g.setColor(nodeColors[i]);
                g.fillOval(x - 8, y - 8, 16, 16);

                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2f));
                g.drawOval(x - 8, y - 8, 16, 16);

                g.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g.getFontMetrics();
                int labelWidth = fm.stringWidth(names[i]) + 16;

                int labelX = x + 13;
                int labelY = y - 12;

                if (labelX + labelWidth > w - 3) {
                    labelX = x - labelWidth - 12;
                }

                g.setColor(new Color(13, 30, 49));
                g.fillRoundRect(labelX, labelY, labelWidth, 24, 10, 10);

                g.setColor(new Color(61, 85, 112));
                g.drawRoundRect(labelX, labelY, labelWidth, 24, 10, 10);

                g.setColor(Color.WHITE);
                g.drawString(names[i], labelX + 8, labelY + 16);
            }

            if (currentRoute == null) {
                g.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g.setColor(new Color(155, 181, 209));
                g.drawString("Calculate a route to highlight its path.",
                    12, h - 14);
            }

            g.dispose();
        }
        
        // Helper to draw a line between two relative coordinates
        private void drawRoad(Graphics2D g, double[][] positions,
                              int a, int b, int w, int h) {
            int x1 = (int) (positions[a][0] * w);
            int y1 = (int) (positions[a][1] * h);
            int x2 = (int) (positions[b][0] * w);
            int y2 = (int) (positions[b][1] * h);

            g.drawLine(x1, y1, x2, y2);
        }
        // Quick way to map a location name back to its index in the array
        private int indexOf(String[] names, String target) {
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(target)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        // Bootstrap the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            AppLogger.initialize();
            AppLogger.info("Taxi Route Optimizer GUI starting.");

             // In production, we'd use GraphFileLoader here.
            Graph graph = new Graph();

            Location bellville = new Location("Bellville");
            Location parow = new Location("Parow");
            Location goodwood = new Location("Goodwood");
            Location capeTown = new Location("Cape Town");
            Location atlantis = new Location("Atlantis");

            graph.addLocation(bellville);
            graph.addLocation(parow);
            graph.addLocation(goodwood);
            graph.addLocation(capeTown);
            graph.addLocation(atlantis);

            graph.addRoad(new Road(bellville, parow, 5.0));
            graph.addRoad(new Road(bellville, goodwood, 8.0));
            graph.addRoad(new Road(parow, goodwood, 2.0));
            graph.addRoad(new Road(parow, capeTown, 18.0));
            graph.addRoad(new Road(goodwood, capeTown, 12.0));
            graph.addRoad(new Road(goodwood, bellville, 8.0));

            RouteFinder finder = new RouteFinder(graph);
            new TaxiRouteGUI(graph, finder);
        });
    }
}
