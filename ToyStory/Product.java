public class Product {
    String uniqueId;
    String name;
    Manufacturer manufacturer;
    double price;
    int quantity;
    Discount discount;

    public Product(String uniqueId, String name, Manufacturer manufacturer, double price, int quantity) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
    }

    double getPriceWithDiscount(Product product) {
        if(product.discount == null) {
            return product.price;
        }
        if(product.discount.discountType.equals(DiscountType.FIXED_DISCOUNT)) {
            return product.price - product.discount.value;
        }
        else {
            return product.price - (product.discount.value/100 * price);
        }
    }
}
