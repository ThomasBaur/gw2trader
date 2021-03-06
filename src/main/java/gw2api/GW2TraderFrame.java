package gw2api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.json.simple.JSONObject;

import au.com.bytecode.opencsv.CSVParser;
import javax.swing.JFormattedTextField;
import java.awt.Font;

public class GW2TraderFrame extends JFrame {

	private JPanel contentPane;
	private final JTable tblData;
	private final TableColumnAdjuster tableColumnAdjuster;
	private final JButton btnFetch;
	private final JButton btnGetTranslations;
	private final JLabel lblStatus;
	private final JComboBox<String> cmbLanguages;
	private JLabel lblMinMargin;
	private JFormattedTextField txtMinMargin;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GW2TraderFrame frame = new GW2TraderFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GW2TraderFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel pnlMain = new JPanel();
		contentPane.add(pnlMain, BorderLayout.CENTER);
		GridBagLayout gbl_pnlMain = new GridBagLayout();
		gbl_pnlMain.columnWidths = new int[] { 0, 0 };
		gbl_pnlMain.rowHeights = new int[] { 0, 0 };
		gbl_pnlMain.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pnlMain.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		pnlMain.setLayout(gbl_pnlMain);

		JScrollPane scrlData = new JScrollPane();
		GridBagConstraints gbc_scrlData = new GridBagConstraints();
		gbc_scrlData.weighty = 1.0;
		gbc_scrlData.weightx = 1.0;
		gbc_scrlData.insets = new Insets(3, 3, 3, 3);
		gbc_scrlData.fill = GridBagConstraints.BOTH;
		gbc_scrlData.gridx = 0;
		gbc_scrlData.gridy = 0;
		pnlMain.add(scrlData, gbc_scrlData);

		tblData = new JTable();
		tblData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblData.setFont(new Font("Consolas", Font.BOLD, 12));
		tblData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = tblData.getSelectedRow();
					int col = tblData.getSelectedColumn();
					if (col == 7) {
						URI uri;
						try {
							String valueAt = (String) tblData.getValueAt(row,
									col);
							uri = new URI(valueAt);
							open(uri);
						} catch (URISyntaxException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		scrlData.setViewportView(tblData);

		tableColumnAdjuster = new TableColumnAdjuster(tblData);

		JPanel pnlSettings = new JPanel();
		contentPane.add(pnlSettings, BorderLayout.EAST);
		GridBagLayout gbl_pnlSettings = new GridBagLayout();
		gbl_pnlSettings.columnWidths = new int[] { 0, 0 };
		gbl_pnlSettings.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_pnlSettings.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_pnlSettings.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		pnlSettings.setLayout(gbl_pnlSettings);

		btnFetch = new JButton("Fetch new");
		btnFetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnFetch.setEnabled(false);
				new ItemFetcher().start();
			}
		});
		GridBagConstraints gbc_btnFetch = new GridBagConstraints();
		gbc_btnFetch.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnFetch.insets = new Insets(3, 3, 5, 3);
		gbc_btnFetch.gridx = 0;
		gbc_btnFetch.gridy = 0;
		pnlSettings.add(btnFetch, gbc_btnFetch);

		btnGetTranslations = new JButton("Get translations (slow)");
		btnGetTranslations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGetTranslations.setEnabled(false);
				new ItemTranslator().start();
			}
		});
		GridBagConstraints gbc_btnGetTranslations = new GridBagConstraints();
		gbc_btnGetTranslations.insets = new Insets(3, 3, 5, 3);
		gbc_btnGetTranslations.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGetTranslations.gridx = 0;
		gbc_btnGetTranslations.gridy = 1;
		pnlSettings.add(btnGetTranslations, gbc_btnGetTranslations);

		JLabel lblLbllanguages = new JLabel("Languages");
		GridBagConstraints gbc_lblLbllanguages = new GridBagConstraints();
		gbc_lblLbllanguages.anchor = GridBagConstraints.WEST;
		gbc_lblLbllanguages.insets = new Insets(3, 6, 5, 3);
		gbc_lblLbllanguages.gridx = 0;
		gbc_lblLbllanguages.gridy = 2;
		pnlSettings.add(lblLbllanguages, gbc_lblLbllanguages);

		cmbLanguages = new JComboBox<>();
		cmbLanguages.setModel(new DefaultComboBoxModel<String>(new String[] {
				"de", "fr" }));
		GridBagConstraints gbc_cmbLanguages = new GridBagConstraints();
		gbc_cmbLanguages.insets = new Insets(3, 3, 5, 3);
		gbc_cmbLanguages.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbLanguages.gridx = 0;
		gbc_cmbLanguages.gridy = 3;
		pnlSettings.add(cmbLanguages, gbc_cmbLanguages);
		
		lblMinMargin = new JLabel("Min. Margin (C)");
		GridBagConstraints gbc_lblMinMargin = new GridBagConstraints();
		gbc_lblMinMargin.insets = new Insets(3, 3, 5, 0);
		gbc_lblMinMargin.anchor = GridBagConstraints.WEST;
		gbc_lblMinMargin.gridx = 0;
		gbc_lblMinMargin.gridy = 4;
		pnlSettings.add(lblMinMargin, gbc_lblMinMargin);
		
		txtMinMargin = new JFormattedTextField();
		txtMinMargin.setValue(10);

		GridBagConstraints gbc_txtMinMargin = new GridBagConstraints();
		gbc_txtMinMargin.insets = new Insets(3, 3, 3, 3);
		gbc_txtMinMargin.anchor = GridBagConstraints.WEST;
		gbc_txtMinMargin.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMinMargin.gridx = 0;
		gbc_txtMinMargin.gridy = 5;
		pnlSettings.add(txtMinMargin, gbc_txtMinMargin);

		JPanel pnlStatus = new JPanel();
		contentPane.add(pnlStatus, BorderLayout.SOUTH);
		GridBagLayout gbl_pnlStatus = new GridBagLayout();
		gbl_pnlStatus.columnWidths = new int[] { 189, 46, 0 };
		gbl_pnlStatus.rowHeights = new int[] { 14, 0 };
		gbl_pnlStatus.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_pnlStatus.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		pnlStatus.setLayout(gbl_pnlStatus);

		lblStatus = new JLabel("");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.weightx = 1.0;
		gbc_lblStatus.insets = new Insets(3, 3, 3, 3);
		gbc_lblStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 0;
		pnlStatus.add(lblStatus, gbc_lblStatus);
	}

	private void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
			}
		} else {
		}
	}

	private List<Item> retrieveItems(int minDemand, int minSupply, int minMargin) {
		List<Item> items = new ArrayList<>();
		CSVParser parser = new CSVParser();
		int fetched = 0;
		int used = 0;
		try {
			URL url = new URL(
					"http://www.gw2spidy.com/api/v0.9/csv/all-items/all");
			try (BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()))) {
				in.readLine();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					fetched++;
					String[] strings = parser.parseLine(inputLine);
					Item item = new Item(strings[0], strings[1],
							Integer.parseInt(strings[8]),
							Integer.parseInt(strings[9]),
							Integer.parseInt(strings[10]),
							Integer.parseInt(strings[11]));
					if (item.getOfferAvailability() > minDemand
							&& item.getSaleAvailability() > minSupply
							&& item.getMargin() > minMargin) {
						items.add(item);
						used++;
						lblStatus.setText("Loaded " + fetched
								+ " items, filter matched by " + used
								+ " items");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(items, new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				double m1 = o1.getMargin();
				double m2 = o2.getMargin();
				if (m2 > m1) {
					return 1;
				} else if (m2 == m1) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		return items;
	}

	private class ItemTranslator extends Thread {
		@Override
		public void run() {
			TableModel model = tblData.getModel();
			if (model != null && model instanceof ItemModel) {
				Gw2Api gw2Api = new RestEasyGw2ApiImpl();

				List<Item> items = ((ItemModel) model).getItems();
				for (Item item : items) {
					JSONObject itemDetails = (JSONObject) gw2Api
							.getItemDetails(item.getId(),
									(String) cmbLanguages.getSelectedItem());
					item.setName(itemDetails.get("name").toString());
					tblData.repaint();
				}
			}
			btnGetTranslations.setEnabled(true);
		}
	}

	private class ItemFetcher extends Thread {
		@Override
		public void run() {
			List<Item> items = retrieveItems(10000, 10000, ((Number)txtMinMargin.getValue()).intValue());
			final TableModel model = new ItemModel(items);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					tblData.setModel(model);
					tableColumnAdjuster.adjustColumns();
					tblData.getColumnModel().getColumn(7)
							.setCellRenderer(new DefaultTableCellRenderer() {
								@Override
								public Component getTableCellRendererComponent(
										JTable table, Object value,
										boolean isSelected, boolean hasFocus,
										int row, int column) {
									Component tableCellRendererComponent = super
											.getTableCellRendererComponent(
													table, value, isSelected,
													hasFocus, row, column);
									if (tableCellRendererComponent instanceof JLabel) {
										JLabel label = (JLabel) tableCellRendererComponent;
										Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
										fontAttributes.put(
												TextAttribute.UNDERLINE,
												TextAttribute.UNDERLINE_ON);
										label.setFont(label.getFont()
												.deriveFont(fontAttributes));
										label.setForeground(Color.blue.darker());
									}
									return tableCellRendererComponent;
								}
							});
					btnFetch.setEnabled(true);
				}
			});
		}
	}

	private static class ItemModel extends AbstractTableModel {
		private List<Item> items;

		public ItemModel(List<Item> items) {
			this.items = items;
		}

		@Override
		public int getRowCount() {
			return items.size();
		}

		@Override
		public int getColumnCount() {
			return 8;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			String reply = "";
			Item item = items.get(rowIndex);

			switch (columnIndex) {
			case 0:
				reply = item.getId();
				break;
			case 1:
				reply = item.getName();
				break;
			case 2:
				reply = Integer.toString(item.getOfferAvailability());
				break;
			case 3:
				reply = Integer.toString(item.getSaleAvailability());
				break;
			case 4:
				reply = toString(item.getMaxOfferUnitPrice());
				break;
			case 5:
				reply = toString(item.getMinSaleUnitPrice());
				break;
			case 6:
				reply = String.format("%4.2fC", item.getMargin());
				break;
			case 7:
				reply = "http://www.gw2spidy.com/item/" + item.getId();
				break;
			}
			return reply;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public String getColumnName(int column) {
			String reply = "";
			switch (column) {
			case 0:
				reply = "ID";
				break;
			case 1:
				reply = "Name";
				break;
			case 2:
				reply = "Demand";
				break;
			case 3:
				reply = "Supply";
				break;
			case 4:
				reply = "Max. Buy Price/Unit";
				break;
			case 5:
				reply = "Min. Sell Price/Unit";
				break;
			case 6:
				reply = "Margin";
				break;
			case 7:
				reply = "Link";
				break;
			}
			return reply;
		}

		private static String toString(int price) {
			String reply = "";
			int gold = price / 10000;
			int silver = price / 100;
			int copper = price - (gold * 10000) - (silver * 100);
			reply = String.format("%2dC", copper);
			if (silver > 0) {
				reply = String.format("%2dS ", silver) + reply;
			} else {
				reply = "    " + reply;
			}
			if (gold > 0) {
				reply = String.format("%dG ", gold) + reply;
			} else {
				reply = "    " + reply;
			}
			return reply;
		}

		public List<Item> getItems() {
			return items;
		}
	}

}
