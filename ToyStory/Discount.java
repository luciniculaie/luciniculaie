
import java.time.LocalDateTime;

public class Discount {
    String name;
    DiscountType discountType;
    double value;
    LocalDateTime lastDateApplied;

    void setAsAppliedNow() {
        lastDateApplied = LocalDateTime.now();
    }

    public Discount() {
    }

    public Discount(String name, DiscountType discountType, double value) {
        this.name = name;
        this.discountType = discountType;
        this.value = value;
    }
}
