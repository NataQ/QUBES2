package id.co.qualitas.qubes.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LashPdfUtils {
    Font calibriRegularWhite, calibriRegular, bigCalibriBold, calibriBold, arialRegular, bigArialBold, arialBold, myriadproRegular;
    int MAX_COLUMN_TABLE = 14;
    int MAX_WIDTH_NO = 4;
    int MAX_WIDTH_CUSTOMER = 10;
    int MAX_WIDTH_OUTLET = 10;
    int MAX_WIDTH_FATUR = 18;
    int MAX_WIDTH_PENJUALAN = 18;
    int MAX_WIDTH_PEMBAYARAN = 30;
    int MAX_WIDTH_SALDO_PIUTANG = 10;
    int MAX_WIDTH_BILYET_GIRO = 30;
    int MAX_WIDTH_TUNAI = 70;

    String volumeUnit = null, weightUnit = null;
    BigDecimal totalVolume = BigDecimal.ZERO, totalWeight = BigDecimal.ZERO;
    BigDecimal totalVolumeFU = BigDecimal.ZERO, totalWeightFU = BigDecimal.ZERO;

    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private static LashPdfUtils instance;
    private Context context;
    private static final String TAG = "LashPdfUtils";

    public static LashPdfUtils getInstance(Context context) {
        if (instance == null) {
            instance = new LashPdfUtils(context);
        }
        return instance;
    }

    private static BaseFont loadCalibri() {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/calibri.otf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            Log.e(TAG, "loadCali: " + e.getMessage());
        }
        return baseFont;
    }

    private static BaseFont loadArial() {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/arial.TTF", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            Log.e(TAG, "loadArial: " + e.getMessage());
        }
        return baseFont;
    }

    private static BaseFont loadMyriad() {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/myriadpro_regular.otf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            Log.e(TAG, "loadMypro: " + e.getMessage());
        }
        return baseFont;
    }

    private static BaseFont loadCalibriBold() {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/calibri_bold.TTF", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            Log.e(TAG, "loadCalBol: " + e.getMessage());
        }
        return baseFont;
    }

    private static BaseFont loadArialBold() {
        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/arial_bold.TTF", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            Log.e(TAG, "loadArBol: " + e.getMessage());
        }
        return baseFont;
    }

    public LashPdfUtils(Context context) {//DeliverySummaryActivityEra
        this.context = context;
    }

    public class BorderEvent implements PdfPTableEvent {
        public void tableLayout(PdfPTable table, float[][] widths,
                                float[] heights, int headerRows, int rowStart,
                                PdfContentByte[] canvases) {
            float width[] = widths[0];
            float x1 = width[0];
            float x2 = width[width.length - 1];
            float y1 = heights[0];
            float y2 = heights[heights.length - 1];
            PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
            cb.rectangle(x1, y1, x2 - x1, y2 - y1);
            cb.stroke();
            cb.resetRGBColorStroke();
        }
    }

    public void initFontPdf() {
        calibriRegularWhite = new Font(loadCalibri(), 8, Color.WHITE);
        calibriRegular = new Font(loadCalibri(), 8);
        arialRegular = new Font(loadArial(), 8);
        myriadproRegular = new Font(loadMyriad(), 8);
        bigCalibriBold = new Font(loadCalibriBold(), 12);
        bigArialBold = new Font(loadArialBold(), 12);
        calibriBold = new Font(loadCalibriBold(), 8);//Font.BOLD
        arialBold = new Font(loadArialBold(), 8);//Font.BOLD
    }

    public void autoEnter(PdfPTable table, PdfPCell cell, int banyak) {
        for (int i = 0; i < banyak; i++) {
            cell = new PdfPCell(new Paragraph(" ", calibriRegular));
            cell.setColspan(table.getNumberOfColumns());
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
    }

    public PdfPTable createPDFHeader() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        Paragraph text;

        cell = new PdfPCell();
        cell.setColspan(3);
        text = new Paragraph("LAPORAN AKTIVITAS SALES - REPRESENTATIVE HARIAN", bigCalibriBold);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(6);
        text = new Paragraph(" ", calibriRegular);
        cell.addElement(text);
        table.addCell(cell);

        return table;
    }
    public PdfPTable createPDFHeaderDetailTable() {
        setFormatSeparator();
        PdfPCell cell;
        Paragraph text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);
//        table.setWidths(new int[]{MAX_WIDTH_NO, MAX_WIDTH_CUSTOMER, MAX_WIDTH_OUTLET, MAX_WIDTH_FATUR, MAX_WIDTH_PENJUALAN, MAX_WIDTH_PEMBAYARAN, MAX_WIDTH_SALDO_PIUTANG});


        cell = new PdfPCell(new Phrase("No.", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("No. Customer", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nama Outlet", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Faktur Tagihan", calibriBold));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Penjualan", calibriBold));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Pembayaran", calibriBold));
        cell.setUseAscender(true);
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Saldo Piutang", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TGL", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nomor", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Jumlah", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase("No. Faktur", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nilai", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Tunai Rp.", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Bilyet Giro", calibriBold));
        cell.setUseAscender(true);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Rupiah", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Bank", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nomor", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("JT Tanggal", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        return table;
    }
    public PdfPTable createPDFFooter(PdfWriter writer, Document document, Image total) {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        PdfPCell cell;
        Paragraph text;

        for (int i = 0; i < 5; i++) {
            cell = new PdfPCell();
            cell.setColspan(6);
            text = new Paragraph((""), calibriRegular);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(text);
            table.addCell(cell);
        }

        cell = new PdfPCell();
        cell.setColspan(3);
        text = new Paragraph("Printed By : " , calibriRegular);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(text);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        text = new Paragraph("Created Date : ", calibriRegular);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(text);
        table.addCell(cell);

        cell = new PdfPCell();
        text = new Paragraph();
        text.add(new Chunk("Page " + String.format("%d", writer.getPageNumber()) + " of", calibriRegular));
        text.add(new Chunk(total, (total.getWidth() / 6) - 2, -6, true));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(text);
        table.addCell(cell);

        table.setTotalWidth(document.right() - document.left());
        table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() + 15, writer.getDirectContent());
        return table;
    }

    public Boolean createPDF(File pdfFile) {
        initFontPdf();
        Boolean isGood = false;
        Document document = new Document(PageSize.A4.rotate());//landscape
        class HeaderFooterPageEvent extends PdfPageEventHelper {
            private PdfTemplate t;
            private Image total;

            @Override
            public void onOpenDocument(PdfWriter writer, Document document) {
                t = writer.getDirectContent().createTemplate(25, 16);
                try {
                    total = Image.getInstance(t);
                } catch (DocumentException de) {
                    throw new ExceptionConverter(de);
                }
            }

            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                createPDFFooter(writer, document, total);
            }

            @Override
            public void onCloseDocument(PdfWriter writer, Document document) {
                int totalLength = String.valueOf(writer.getPageNumber()).length();
                int totalWidth = totalLength * 5;
                if (t == null) {
                    t = writer.getDirectContent().createTemplate(25, 16);
                    try {
                        total = Image.getInstance(t);
                    } catch (DocumentException de) {
                        throw new ExceptionConverter(de);
                    }
                }
                ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                        new Phrase(String.valueOf(writer.getPageNumber() - 1), calibriRegular),
                        totalWidth, 6, 0);
            }
        }
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.setMargins(30, 30, 50, 50);
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            document.open();

            document.add(createPDFHeader());
            document.add(createPDFHeaderDetailTable());
            isGood = true;

        } catch (Exception e) {
            Log.e("PDF", e.getMessage());
            e.printStackTrace();
            isGood = false;
        }
        document.close();
        return isGood;
    }

//    public Boolean createPDFReceipt(File pdfFile, DoHeader doHeader, ArrayList<DoDetail> doDetailArrayList, boolean notDelivered) {
//        initFontPdf();
//        Boolean isGood = false;
//        Rectangle pageSize = new Rectangle(288, 2880); //3 x 30 inc
//        Document document = new Document(pageSize);
//        try {
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
//            document.setMargins(10, 10, 5, 5);
//            document.open();
//
//            document.add(createReceiptHeader(doHeader));
//            document.add(createReceiptTop(doHeader));
//            document.add(createReceiptHeaderDetailTable(doDetailArrayList));
//            document.add(createReceiptSign(doHeader, notDelivered));
//            document.add(createReceiptListImei(doDetailArrayList));
////			}
//            isGood = true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            isGood = false;
//        }
//        document.close();
//        return isGood;
//    }

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}