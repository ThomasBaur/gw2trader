package test

import gw2api.Gw2Api;

import com.xlson.groovycsv.CsvParser

class ParseCsvData {

    public static void main(String[] args) {
        Gw2Api gw2 = new Gw2Api()
        
        def items = []
        def url = 'http://www.gw2spidy.com/api/v0.9/csv/all-items/all'.toURL()
        def content = url.getText(requestProperties: ['User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0'])
        def data = CsvParser.parseCsv(content)
        data.each {
            Item item = new Item(id: it.data_id,
                name: it.name,
            maxOfferUnitPrice: Integer.parseInt(it.max_offer_unit_price),
            minSaleUnitPrice: Integer.parseInt(it.min_sale_unit_price),
            offerAvailability: Integer.parseInt(it.offer_availability),
            saleAvailability: Integer.parseInt(it.sale_availability))
            if (item.offerAvailability > 10000 && item.saleAvailability > 10000 && item.margin > 1) {
                items.add(item)
            }
        }
		println "id, name, de-name, max. kaufgebot, min. verkaufsangebot, gewinn"
        items.sort{a,b -> (a.margin <=> b.margin) *-1}.each {
            def object = gw2.getItemDetails(it.id, 'de')
        
            println "$it.id, $it.name, $object.name, $it.maxOfferUnitPrice, $it.minSaleUnitPrice, $it.margin"
        }
    }
}

class Item {
    String id
    String name
    int maxOfferUnitPrice
    int minSaleUnitPrice
    int offerAvailability
    int saleAvailability

    double getNetPrice() {
        return minSaleUnitPrice * 0.85
    }

    double getMargin() {
        return netPrice - maxOfferUnitPrice
    }

    int getMarginPercentage() {
        return ((int)(margin / netPrice) * 100)
    }
}
