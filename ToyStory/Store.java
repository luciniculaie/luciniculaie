
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Store {
    private static Store single_instance = null;
    String name;
    Currency currency;
    ArrayList<Product> products;
    ArrayList<Manufacturer> manufacturers;
    ArrayList<Discount> discounts;
    ArrayList<Currency> allCurencies;

    public Store() {

    }

    public static Store getInstance(String name) {
        if (single_instance == null)
            single_instance = new Store(name);
        return single_instance;
    }

    public Store(String name) {
        this.name = name;
        products = new ArrayList<Product>();
        manufacturers = new ArrayList<Manufacturer>();
        allCurencies = new ArrayList<Currency>();
        discounts = new ArrayList<Discount>();
        allCurencies.add(new Currency("EUR", "€", 1));
        this.currency = allCurencies.get(0);
    }

    Discount createDiscount(DiscountType discountType, String name, double value) {
        return new Discount(name, discountType, value);
    }

    Currency createCurrency(String name, String symbol, double parityToEur) {
        return new Currency(name, symbol, parityToEur);
    }

    ArrayList<Product> getProductsByManufacturer(Manufacturer manufacturer) {
        ArrayList<Product> productsByManufacturer = new ArrayList<Product>();
        for (Product product : this.products) {
            if (product.manufacturer.name.equals(manufacturer.name))
                productsByManufacturer.add(product);
        }
        return productsByManufacturer;
    }

    void listInfoAboutProduct(Product prod) {
        System.out.println(prod.uniqueId + "," + prod.name + "," + prod.manufacturer.name + ","
                + this.currency.symbol + prod.getPriceWithDiscount(prod) + "," + prod.quantity);
    }

    public static class DuplicateProductException extends Exception {
        public DuplicateProductException() {
            super("Product already exists.");
        }
    }

    void addProduct(Product product) throws DuplicateProductException {
        for (Product theProduct : this.products) {
            if (theProduct.uniqueId.equals(product.uniqueId))
                throw new DuplicateProductException();
        }
        this.products.add(product);
    }

    Manufacturer addManufacturer(Manufacturer manufacturer) {
        Manufacturer returnManufacturer;
        for (Manufacturer theManufacturer : this.manufacturers) {
            if (theManufacturer.name.equals(manufacturer.name)) {
                returnManufacturer = theManufacturer;
                return returnManufacturer;
            }
        }
            this.manufacturers.add(manufacturer);
            return manufacturer;
    }//metoda aceasta va returna noul manufacturer creat sau manufacturer-ul deja existent

    void printCSV(String filename) {
        try (
                BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename));
                CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT.withHeader("uniq_id", "product_name",
                        "manufacturer", "price", "number_available_in_stock"));
        ) {
            for(Product prod: this.products)
                csvPrinter.printRecord(prod.uniqueId,prod.name, prod.manufacturer.name,
                        this.currency.symbol + prod.getPriceWithDiscount(prod), prod.quantity);
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readCSV(String filename) throws IOException {
        try (
                BufferedReader reader = Files.newBufferedReader((Paths.get(filename)).toAbsolutePath());
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("uniq_id", "product_name",
                        "manufacturer", "price", "number_available_in_stock").withFirstRecordAsHeader());
                //mi-am setat header-ul in parser ca sa pot extrage campurile mai usor
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String uniq_id = csvRecord.get("uniq_id");
                String product_name = csvRecord.get("product_name");
                String string_price = csvRecord.get("price");
                double price = convertPrice(string_price, this.currency);
                String manufacturer_name = csvRecord.get("manufacturer");
                Manufacturer manufacturer1 = new Manufacturer(manufacturer_name);
                int quantity = Integer.parseInt(csvRecord.get("number_available_in_stock").split("NEW")[0]
                        .substring(0, csvRecord.get("number_available_in_stock").split("NEW")[0].length() - 1));
                //parsam toate informatiile necesare pentru a crea un produs
                try {
                    manufacturer1 = this.addManufacturer(manufacturer1);
                    Product newProduct = new ProductBuilder().setUniqueID(uniq_id).setName(product_name)
                            .setManufacturer(manufacturer1).setPrice(price).setQuantity(quantity).build();
                    this.addProduct(newProduct);
                    manufacturer1.increaseCountProducts();
                } catch (DuplicateProductException e) {
                    if(manufacturer1.countProducts == 0)
                        this.manufacturers.remove(manufacturer1);
                    //daca un manufacturer a fost adaugat dar produsul deja
                    //exista vom sterge manufacturer-ul din ArrayList
                    e.printStackTrace();
                }
            }
        }
    }

    public static class DiscountNotFoundException extends Exception {
        public DiscountNotFoundException() {
            super("Discount was not found.");
        }
    }
    public static class ProductNotFoundException extends Exception {
        public ProductNotFoundException() {
            super("Product was not found.");
        }
    }

    public static class NegativePriceException extends Exception {
        public NegativePriceException() {
            super("Cannot apply discount on product.");
        }
    }

    void calculateTotal(String[] uniqIDS) throws ProductNotFoundException {
        double sum = 0;
        int flag = 0;
        for (int i = 1; i < uniqIDS.length; i++) {
            for (Product prod : this.products)
                if (prod.uniqueId.equals(uniqIDS[i])) {
                    sum += prod.getPriceWithDiscount(prod) * prod.quantity;
                    flag = 1;
                }
                if (flag == 0)
                    throw new ProductNotFoundException();

        }
        System.out.println(this.currency.symbol + sum);
    }

    void applyPercentageDiscount(double value) throws DiscountNotFoundException {
        int flag = 0;
        Discount theDiscount = null;
        for (Discount discount : this.discounts)
            if (discount.discountType.equals(DiscountType.PERCENTAGE_DISCOUNT)
                    && discount.value == value) {
                theDiscount = discount;
                flag = 1;
            }
        if (flag == 0)
            throw new DiscountNotFoundException();
        for (Product prod : this.products) {
            prod.discount = this.createDiscount(DiscountType.PERCENTAGE_DISCOUNT,
                    theDiscount.name, value);
            prod.discount.setAsAppliedNow();
            theDiscount.setAsAppliedNow();
        }
    }

    void applyFixedDiscount(double value) throws DiscountNotFoundException,NegativePriceException {
        int flag = 0;
        Discount theDiscount = null;
        for (Discount discount : this.discounts)
            if (discount.discountType.equals(DiscountType.FIXED_DISCOUNT) && discount.value == value) {
                theDiscount = discount;
                flag = 1;
            }
        if (flag == 0)
            throw new DiscountNotFoundException();
        for (Product prod : this.products) {
            if (prod.price - value > 0) {
                prod.discount = this.createDiscount(DiscountType.FIXED_DISCOUNT,
                        theDiscount.name, value);
                prod.discount.setAsAppliedNow();
                theDiscount.setAsAppliedNow();
            }
            else
                throw new NegativePriceException();
        }
    }

    double convertPrice(String price, Currency currency) {
        String symbol = price.substring(0, 1);
        price = price.replace(",", "");
        double onlyPrice = Double.parseDouble(price.substring(1));
        for(Currency theCurrency : allCurencies)
            if(theCurrency.symbol.equals(symbol)) {
                onlyPrice = onlyPrice * theCurrency.parityToEur;
                break;
            }
        return onlyPrice/currency.parityToEur;
    }
}
