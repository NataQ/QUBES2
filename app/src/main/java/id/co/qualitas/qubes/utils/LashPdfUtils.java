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

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class LashPdfUtils {
    Font calibriRegularWhite, calibriRegular, bigCalibriBold, bigCalibri, calibriBold, arialRegular, bigArialBold, arialBold, myriadproRegular;
    int MAX_COLUMN_TABLE = 15;
    int MAX_WIDTH_NO = 4;
    int MAX_WIDTH_CUSTOMER = 7;
    int MAX_WIDTH_OUTLET = 8;
    int MAX_WIDTH_TGL = 6;
    int MAX_WIDTH_NO_TAGIHAN = 6;
    int MAX_WIDTH_JMLH = 6;
    int MAX_WIDTH_NO_FAKTUR = 7;
    int MAX_WIDTH_NILAI = 7;
    int MAX_WIDTH_TUNAI = 7;
    int MAX_WIDTH_GIRO = 7;
    int MAX_WIDTH_TF = 7;
    int MAX_WIDTH_RETUR = 7;
    int MAX_WIDTH_LAIN = 7;
    int MAX_WIDTH_SALDO_PIUTANG = 8;
    int MAX_WIDTH_KET = 6;

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
        calibriRegularWhite = new Font(loadCalibri(), 5, Color.WHITE);
        calibriRegular = new Font(loadCalibri(), 5);
        arialRegular = new Font(loadArial(), 5);
        myriadproRegular = new Font(loadMyriad(), 5);
        bigCalibriBold = new Font(loadCalibriBold(), 10);
        bigCalibri = new Font(loadCalibri(), 10);
        bigArialBold = new Font(loadArialBold(), 10);
        calibriBold = new Font(loadCalibriBold(), 5);//Font.BOLD
        arialBold = new Font(loadArialBold(), 5);//Font.BOLD
    }

    public void autoEnter(PdfPTable table, PdfPCell cell, int banyak) {
        for (int i = 0; i < banyak; i++) {
            cell = new PdfPCell(new Paragraph(" ", calibriRegular));
            cell.setColspan(table.getNumberOfColumns());
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
    }

    public PdfPTable createPDFTitle() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        Paragraph text;

        cell = new PdfPCell();
        text = new Paragraph("PT. ASIASEJAHTERA PERDANA PHARMACEUTICAL", bigCalibri);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        table.addCell(cell);

        return table;
    }

    public PdfPTable createPDFTop() {
        PdfPCell cell;
        Paragraph text;

        PdfPTable mainTable = new PdfPTable(4);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new int[]{15, 35, 15, 35});
//
//        PdfPTable leftTable = new PdfPTable(3);
//        leftTable.setWidthPercentage(100);
//        leftTable.setWidths(new int[]{28, 2, 70});

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        text = new Paragraph("", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph("EOD", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        text = new Paragraph(": " + Helper.getTodayDate(Constants.DATE_FORMAT_2), bigCalibri);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph("KANTOR / DEPO", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph(": " + "DKI", bigCalibri);//BD01
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph("NO.LASH", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph(": " + "515920230113", bigCalibri);//BD01
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph("NAMA SALES", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph(": " + "CHRIS (PT. AHEB)", bigCalibri);//SES G.PUSAT PALEM
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph("TANGGAL", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Paragraph(": " + Helper.getTodayDate(Constants.DATE_FORMAT_1), bigCalibri);//SES G.PUSAT PALEM
        cell.addElement(text);
        mainTable.addCell(cell);

        return mainTable;
    }

    public PdfPTable createPDFHeader() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        Paragraph text;

        cell = new PdfPCell();
        text = new Paragraph("LAPORAN AKTIVITAS SALES - REPRESENTATIVE HARIAN", bigCalibriBold);
        cell.setBorder(Rectangle.BOX);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(text);
        table.addCell(cell);

        return table;
    }

    public PdfPTable createPDFSign() {
        PdfPCell cell;
        Paragraph text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{MAX_WIDTH_NO, MAX_WIDTH_CUSTOMER, MAX_WIDTH_OUTLET,
                MAX_WIDTH_TGL, MAX_WIDTH_NO_TAGIHAN, MAX_WIDTH_JMLH,
                MAX_WIDTH_NO_FAKTUR, MAX_WIDTH_NILAI, MAX_WIDTH_TUNAI,
                MAX_WIDTH_GIRO, MAX_WIDTH_TF, MAX_WIDTH_RETUR,
                MAX_WIDTH_LAIN, MAX_WIDTH_SALDO_PIUTANG, MAX_WIDTH_KET});

        //ttd
        cell = new PdfPCell(new Phrase("Serah Terima Faktur", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(9);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Penyelesaian Laporan", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Disediakan Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Diterima Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Dibuat Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Diterima Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Diketahui Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //enter 1

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));//space middle
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        //enter 2

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));//space middle
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        //name

        cell = new PdfPCell(new Phrase("ADM", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("CHRIS (PT. AHEB)", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("CHRIS (PT. AHEB)", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));//space middle
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("ADM", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("KASIR", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DSM", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);


        return table;
    }

    public PdfPTable createPDFHeaderTable() {
        setFormatSeparator();
        PdfPCell cell;
        Paragraph text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{MAX_WIDTH_NO, MAX_WIDTH_CUSTOMER, MAX_WIDTH_OUTLET,
                MAX_WIDTH_TGL, MAX_WIDTH_NO_TAGIHAN, MAX_WIDTH_JMLH,
                MAX_WIDTH_NO_FAKTUR, MAX_WIDTH_NILAI, MAX_WIDTH_TUNAI,
                MAX_WIDTH_GIRO, MAX_WIDTH_TF, MAX_WIDTH_RETUR,
                MAX_WIDTH_LAIN, MAX_WIDTH_SALDO_PIUTANG, MAX_WIDTH_KET});

        autoEnter(table, new PdfPCell(), 1);

        cell = new PdfPCell(new Phrase("LAPORAN AKTIVITAS SALES - REPRESENTATIVE HARIAN", bigCalibriBold));
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(MAX_COLUMN_TABLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("No.", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("No. Cust", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nama Outlet", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
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
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Ket.", calibriBold));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Tgl", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("No.", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Jumlah", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("No. Faktur", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nilai", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Tunai", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Giro/Cheque", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Transfer", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Retur", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Lain2", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //detail

        double totalJumlah = 0.0, totalNilai = 0.0, totalTunai = 0.0, totalGiro = 0.0, totalTf = 0.0, totalRetur = 0.0, totalLain = 0.0, totalSaldo = 0.0;
        for (int i = 0; i < 6; i++) {
            totalJumlah = totalJumlah + (i + 3);
            totalNilai = totalNilai + (72000 + i + 1);
            totalTunai = totalTunai + (0);
            totalGiro = totalGiro + (0);
            totalTf = totalTf + (0);
            totalRetur = totalRetur + (0);
            totalLain = totalLain + (0);
            totalSaldo = totalSaldo + (72000 + i + 1);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("20002000" + String.valueOf(i + 1), calibriRegular));//no cust
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("karel" + String.valueOf(i + 1), calibriRegular));//name outlet
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Helper.getTodayDate(Constants.DATE_FORMAT_1) + String.valueOf(i + 1), calibriRegular));//tgl
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("000" + String.valueOf(i + 1), calibriRegular));//no tagihan
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format((i + 3)), calibriRegular));//jumlah
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("105159000002" + String.valueOf(i + 2), calibriRegular));//no faktur
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format((72000 + i + 1)), calibriRegular));//nilai
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format(0), calibriRegular));//tunai
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format(0), calibriRegular));//giro
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format(0), calibriRegular));//tf
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format(0), calibriRegular));//retur
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format(0), calibriRegular));//lain2
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(format.format(72000 + i + 1), calibriRegular));//saldo piutang
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));//ket
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);
        }

        //total

        cell = new PdfPCell(new Phrase("TOTAL", calibriBold));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TAGIHAN", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalJumlah), calibriRegular));//total jumlah
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PENJ", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalNilai), calibriRegular));//total nilai
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalTunai), calibriRegular));//tunai
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalGiro), calibriRegular));//giro
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalTf), calibriRegular));//tf
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalRetur), calibriRegular));//retur
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalLain), calibriRegular));//lain2
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(format.format(totalSaldo), calibriRegular));//saldo piutang
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));//ket
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        autoEnter(table, cell, 3);

        return table;
    }

    public PdfPTable createPDFFooter(PdfWriter writer, Document document, Image total) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        Paragraph text;

        for (int i = 0; i < 3; i++) {
            cell = new PdfPCell();
            text = new Paragraph((""), calibriRegular);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(text);
            table.addCell(cell);
        }

        cell = new PdfPCell();
        text = new Paragraph();
        text.add(new Chunk("Halaman " + String.format("%d", writer.getPageNumber()) + " dari", calibriRegular));
//        text.add(new Chunk(total, (total.getWidth() / 6) - 2, -6, true)); offset x kiri kanan, offset y atas bawah
        text.add(new Chunk(total, (total.getWidth() / 9) - 7, -6, true));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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
                t = writer.getDirectContent().createTemplate(25, 16);//25,16
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
                int totalWidth = totalLength * 5;//5
                if (t == null) {
                    t = writer.getDirectContent().createTemplate(25, 16);//25, 16
                    try {
                        total = Image.getInstance(t);
                    } catch (DocumentException de) {
                        throw new ExceptionConverter(de);
                    }
                }
                ColumnText.showTextAligned(t, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1), calibriRegular), totalWidth, 6, 0);
            }
        }
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.setMargins(30, 30, 50, 50);
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            document.open();

            document.add(createPDFTitle());
            document.add(createPDFTop());
            document.add(createPDFHeaderTable());
            document.add(createPDFSign());
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