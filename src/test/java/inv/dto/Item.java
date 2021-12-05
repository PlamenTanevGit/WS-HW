package inv.dto;

import lombok.NoArgsConstructor;
import lombok.NonNull;


public class Item {
    private String name;
    private double price_for_quantity;
    private String quantity_unit;
    private double price;
    private String currency;


    public double vat_percent;

    public double amount_vat;

    public double price_total;

    public double quantity;

    public Item(String name, double price_for_quantity, String quantity_unit, double price, String currency) {
        this.name=name;
        this.price_for_quantity=price_for_quantity;
        this.quantity_unit=quantity_unit;
        this.price=price;
        this.currency=currency;
    }

    public Item(String name, double price_for_quantity, String quantity_unit, double price, String currency,
                int vat, double amunt_vat, int total, int quantity) {
        this.name=name;
        this.price_for_quantity=price_for_quantity;
        this.quantity_unit=quantity_unit;
        this.price=price;
        this.currency=currency;
        this.vat_percent=vat;
        this.amount_vat=amunt_vat;
        this.price_total=total;
        this.quantity=quantity;
    }

}


