package es.upm.miw.tfm.automundo.domain.services.utils;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.Revision;

import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class PdfInvoiceBuilder {
    private static final String[] TABLE_COLUMNS_HEADERS = {"Cant.", "Descripción", "Dto.%", "Price €"};
    private static final float[] TABLE_COLUMNS_SIZES_INVOICES = {10, 100, 15, 35};
    private static final String PATH = "/automundo-pdfs/invoices/";
    private static final String FILE = "invoice-";


    public byte[] generateInvoice(Revision revision) {

        PdfCoreBuilder pdf = new PdfCoreBuilder(PATH, FILE + revision.getReference());
        pdf.a4Size();
        pdf.head();

        Customer customer = revision.getVehicle().getCustomer();

        pdf.paragraphEmphasized("FACTURA PROFORMA");
        pdf.paragraph("Cliente: " + customer.getCompletedName());
        pdf.paragraph("Fecha de la revisión: " + revision.getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        pdf.paragraph("Estado: " + revision.getStatusName());

        PdfTableBuilder table = pdf.table(TABLE_COLUMNS_SIZES_INVOICES).tableColumnsHeader(TABLE_COLUMNS_HEADERS);
        revision.getReplacementsUsed().forEach(replacementUsed -> table.tableCells(replacementUsed.getQuantity().toString(), replacementUsed.getReplacement().getName(), replacementUsed.getDiscount().toString(),
                    replacementUsed.getTotalPrice().setScale(2, RoundingMode.HALF_UP) + "€")
        );

        table.tableColspanRight("Base tax: " + revision.getBaseTax().setScale(2, RoundingMode.HALF_UP) + "€").buildTable();
        table.tableColspanRight("Tax value (21% IVA): " + revision.getTaxValue().setScale(2, RoundingMode.HALF_UP) + "€").buildTable();
        table.tableColspanRight("Precio Final: " + revision.getCost().setScale(2, RoundingMode.HALF_UP) + "€").buildTable();
        pdf.br();

        pdf.qrCode(revision.getReference()).line();

        return pdf.foot().build();
    }
}
