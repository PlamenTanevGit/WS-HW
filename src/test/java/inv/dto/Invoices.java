package inv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Invoices {
   public String type;
    public String to_name;
    public String to_country;
    public String to_town;
    public String to_address;
    public long to_bulstat;
    public boolean to_is_reg_vat;
    public String to_vat_number;
    public String to_mol;
    public boolean is_to_person;
    public long to_egn;
    public String recipient;
    public String payment_currency;
    public String payment_method;
    public String[] payment_options= new String[1];
    public String status;
    public int payment_amount;
    public int payment_amount_base;
    public int  payment_amount_vat;
    public int  payment_amount_reduction;
    public int  payment_amount_total;
    public Item[] items;
}
