import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {
    
    public static void main(String[] args) throws IOException {
        Store store = Store.getInstance("Noriel");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("listcurrencies")) {
                if (store.allCurencies.isEmpty())
                    System.out.println("No currencies to list.");
                for (Currency cur : store.allCurencies) {
                    System.out.print(cur.name + " " + cur.parityToEur + "\n");
                }

            } else if (line.equals("getstorecurrency"))
                System.out.print(store.currency.name + "\n");

            else if (line.startsWith("addcurrency")) {
                String name = line.split(" ")[1];
                String symbol = line.split(" ")[2];
                double parityToEur = Double.parseDouble(line.split(" ")[3]);
                store.allCurencies.add(store.createCurrency(name, symbol, parityToEur));

            } else if (line.startsWith("setstorecurrency")) {
                String name = line.substring(17);
                for (Currency cur : store.allCurencies)
                    if (cur.name.equals(name)) {
                        for (Product prod : store.products)
                            prod.price = prod.price * store.currency.parityToEur / cur.parityToEur;
                        store.currency = cur;
                        break;
                    }

            } else if (line.startsWith("updateparity")) {
                String name = line.substring(13, 16);
                double newParity = Double.parseDouble(line.substring(17));
                for (Currency cur : store.allCurencies)
                    if (cur.name.equals(name)) {
                        cur.updateParity(newParity);
                        break;
                    }

            } else if (line.equals("listproducts")) {
                for (Product prod : store.products)
                    store.listInfoAboutProduct(prod);

            } else if (line.startsWith("showproduct")) {
                try {
                    String uniqID = line.substring(12);
                    int flag = 0;
                    for (Product prod : store.products)
                        if (prod.uniqueId.equals(uniqID)) {
                            flag = 1;
                            store.listInfoAboutProduct(prod);
                            break;
                        }
                    if (flag == 0)
                        throw new Store.ProductNotFoundException();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (line.equals("listmanufacturers")) {
                for (Manufacturer manufacturer : store.manufacturers)
                    System.out.println(manufacturer.name);

            } else if (line.startsWith("listproductsbymanufacturer")) {
                String name = line.substring(27);
                ArrayList<Product> productsByManufacturer = store.getProductsByManufacturer(new Manufacturer(name));
                for(Product product : productsByManufacturer)
                    store.listInfoAboutProduct(product);

            } else if (line.equals("listdiscounts")) {
                if (store.discounts.isEmpty())
                    System.out.println("No discounts to list.");
                for (Discount discount : store.discounts)
                    if (discount.lastDateApplied == null)
                        System.out.println(discount.discountType + " " + discount.value + " " + discount.name
                                + " Discount was never applied");
                    else
                        System.out.println(discount.discountType + " " + discount.value + " " + discount.name
                                + " " + discount.lastDateApplied);

            } else if (line.startsWith("adddiscount")) {
                String name = line.split(" ")[3];
                for(int i = 4; i < line.split(" ").length; i++)
                    name = name + " " + line.split(" ")[i];
                double value = Double.parseDouble(line.split(" ")[2]);
                if (line.charAt(12) == 'P') {
                    store.discounts.add(store.createDiscount(DiscountType.PERCENTAGE_DISCOUNT, name, value));
                } else
                    store.discounts.add(store.createDiscount(DiscountType.FIXED_DISCOUNT, name, value));

            } else if (line.startsWith("applydiscount")) {
                double value = Double.parseDouble(line.split(" ")[2]);
                if (line.split(" ")[1].equals("PERCENTAGE")) {
                    try {
                            store.applyPercentageDiscount(value);
                        }
                    catch (Store.DiscountNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        store.applyFixedDiscount(value);
                        }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (line.startsWith("savecsv")) {
                store.printCSV(line.split(" ")[1]);

            } else if (line.startsWith("loadcsv")) {
                store.readCSV(line.split(" ")[1]);

            } else if (line.startsWith("calculatetotal")) {
                String[] uniqIDS = line.split(" ");
                try {
                    store.calculateTotal(uniqIDS);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(line.startsWith("quit")) {
                return;
            } else if(line.startsWith("exit")) {
                return;
            }
        }
    }
}
