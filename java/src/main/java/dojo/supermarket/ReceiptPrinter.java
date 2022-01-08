package dojo.supermarket;

import dojo.supermarket.model.*;
import java.util.Collections;
import java.util.Locale;

public class ReceiptPrinter {

    private final int columns;

    private static final int DEFAULT_COLUMN_NUMBER = 40;

    public ReceiptPrinter() {
        this(DEFAULT_COLUMN_NUMBER);
    }

    public ReceiptPrinter(int columns) {
        this.columns = columns;
    }

    private void addItemsToTheReceipt(StringBuilder resultStream, Receipt receipt){
        receipt.getItems().forEach(item -> resultStream.append(presentReceiptItem(item)));
    }

    private void addDiscountsToTheReceipt(StringBuilder resultStream, Receipt receipt){
        receipt.getDiscounts().forEach(discount -> resultStream.append(presentDiscount(discount)));
    }

    public String printReceipt(Receipt receipt) {
        StringBuilder result = new StringBuilder();
        addItemsToTheReceipt(result, receipt);
        addDiscountsToTheReceipt(result, receipt);

        result.append("\n");
        result.append(presentTotal(receipt));
        return result.toString();
    }

    private String presentReceiptItem(ReceiptItem item) {
        String totalPricePresentation = presentPrice(item.getTotalPrice());
        String name = item.getProduct().getName();

        String line = formatLineWithWhitespace(name, totalPricePresentation);

        if (item.getQuantity() != 1) {
            line += "  " + presentPrice(item.getPrice()) + " * " + presentQuantity(item) + "\n";
        }
        return line;
    }

    private String presentDiscount(Discount discount) {
        String name = discount.getDescription() + "(" + discount.getProduct().getName() + ")";
        String value = presentPrice(discount.getDiscountAmount());

        return formatLineWithWhitespace(name, value);
    }

    private String presentTotal(Receipt receipt) {
        String name = "Total: ";
        String value = presentPrice(receipt.getTotalPrice());
        return formatLineWithWhitespace(name, value);
    }

    private int calculateWhiteSpaceSize(String name, String value){
        return this.columns - name.length() - value.length();
    }

    private void addWhitespaces(StringBuilder line, int whitespaceSize){
        line.append(String.join("", Collections.nCopies(whitespaceSize, " ")));
    }

    private String formatLineWithWhitespace(String name, String value) {
        StringBuilder line = new StringBuilder();
        line.append(name);
        addWhitespaces(line, calculateWhiteSpaceSize(name, value));
        line.append(value);
        line.append('\n');
        return line.toString();
    }

    private static String presentPrice(double price) {
        return String.format(Locale.UK, "%.2f", price);
    }

    private static String presentQuantity(ReceiptItem item) {
        return ProductUnit.Each.equals(item.getProduct().getUnit())
                ? String.format("%x", (int)item.getQuantity())
                : String.format(Locale.UK, "%.3f", item.getQuantity());
    }

}
