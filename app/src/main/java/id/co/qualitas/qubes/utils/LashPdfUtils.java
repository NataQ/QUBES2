package id.co.qualitas.qubes.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.DepoRegion;
import id.co.qualitas.qubes.model.StartVisit;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class LashPdfUtils {
    Font calibriRegularWhite, calibriRegular, bigCalibriBold, bigCalibri, calibriBold, arialRegular, bigArialBold, arialBold, myriadproRegular;
    int SIZE_FONT_NORMAL = 5;
    int SIZE_FONT_BIG = 10;
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
    private User user;
    private StartVisit startVisit;

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
        calibriRegularWhite = new Font(loadCalibri(), SIZE_FONT_NORMAL, Color.WHITE);
        calibriRegular = new Font(loadCalibri(), SIZE_FONT_NORMAL);
        arialRegular = new Font(loadArial(), SIZE_FONT_NORMAL);
        myriadproRegular = new Font(loadMyriad(), SIZE_FONT_NORMAL);
        bigCalibriBold = new Font(loadCalibriBold(), SIZE_FONT_BIG);
        bigCalibri = new Font(loadCalibri(), SIZE_FONT_BIG);
        bigArialBold = new Font(loadArialBold(), SIZE_FONT_BIG);
        calibriBold = new Font(loadCalibriBold(), SIZE_FONT_NORMAL);//Font.BOLD
        arialBold = new Font(loadArialBold(), SIZE_FONT_NORMAL);//Font.BOLD
    }

    public void autoEnter(PdfPTable table, PdfPCell cell, int banyak) {
        for (int i = 0; i < banyak; i++) {
            cell = new PdfPCell(new Phrase(" ", calibriRegular));
            cell.setColspan(table.getNumberOfColumns());
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
    }

    public PdfPTable createPDFTitle() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("PT. ASIASEJAHTERA PERDANA PHARMACEUTICAL", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    private String getDepo() {
        String depo = "";
        if (user.getDepoRegionList() != null) {
            for (int i = 0; i < user.getDepoRegionList().size(); i++) {
                DepoRegion depoRegion = user.getDepoRegionList().get(i);
                depo = depo + depoRegion.getDepo_name();
                if (i != user.getDepoRegionList().size() - 1) {
                    depo = depo.concat("\n");
                }
            }
        }
        return depo;
    }

    private String getRegion() {
        String depo = "";
        if (user.getDepoRegionList() != null) {
            for (int i = 0; i < user.getDepoRegionList().size(); i++) {
                DepoRegion depoRegion = user.getDepoRegionList().get(i);
                depo = depo + depoRegion.getRegion_name();
                if (i != user.getDepoRegionList().size() - 1) {
                    depo = depo.concat("-");
                }
            }
        }
        return depo;
    }

    public PdfPTable createPDFTop(String nameLash) {
        PdfPCell cell;
        Phrase text;

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
        text = new Phrase("", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("EOD", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        text = new Phrase(": " + Helper.getTodayDate(Constants.DATE_FORMAT_2), bigCalibri);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("KANTOR / DEPO", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase(": " + getDepo(), bigCalibri);//BD01
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("NO.LASH", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase(": " + nameLash, bigCalibri);//BD01
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("NAMA SALES", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase(": " + user.getFull_name() + "(" + getRegion() + ")", bigCalibri);//SES G.PUSAT PALEM
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("TANGGAL", bigCalibri);
        cell.addElement(text);
        mainTable.addCell(cell);

        String date;
        try {
            startVisit = new Database(context).getLastStartVisit();
            date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_TYPE_15, startVisit.getDate());
        } catch (Exception e) {
            date = Helper.getTodayDate(Constants.DATE_TYPE_15);
        }

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase(": " + date, bigCalibri);//SES G.PUSAT PALEM
        cell.addElement(text);
        mainTable.addCell(cell);

        return mainTable;
    }

    public PdfPTable createPDFSign() {
        PdfPCell cell;
        Phrase text;
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

        String name = SessionManagerQubes.getUserProfile() != null ? SessionManagerQubes.getUserProfile().getFullName() : "";

        cell = new PdfPCell(new Phrase(name, calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(name, calibriRegular));
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

    public PdfPTable createPDFHeaderTable(List<Map> lashList) {
        setFormatSeparator();
        PdfPCell cell;
        Phrase text;
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

        cell = new PdfPCell(new Phrase("No. Order", calibriBold));
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

        cell = new PdfPCell(new Phrase("Giro", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cheque", calibriBold));
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

        cell = new PdfPCell(new Phrase("Lain2", calibriBold));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //detail
        String date;
        Map detail;
        double totalJumlah = 0.0, totalNilai = 0.0, totalTunai = 0.0, totalGiro = 0.0, totalCheque = 0.0, totalTf = 0.0, totalLain = 0.0, totalSaldo = 0.0;
        for (int i = 0; i < lashList.size(); i++) {
            detail = lashList.get(i);
            totalJumlah = totalJumlah + (detail.get("jumlah") != null ? (double) detail.get("jumlah") : 0);
            totalNilai = totalNilai + (detail.get("nilai") != null ? (double) detail.get("nilai") : 0);
            totalTunai = totalTunai + (detail.get("tunai") != null ? (double) detail.get("tunai") : 0);
            totalGiro = totalGiro + (detail.get("giro") != null ? (double) detail.get("giro") : 0);
            totalCheque = totalCheque + (detail.get("cheque") != null ? (double) detail.get("cheque") : 0);
            totalTf = totalTf + (detail.get("transfer") != null ? (double) detail.get("transfer") : 0);
            totalLain = totalLain + (detail.get("lain2") != null ? (double) detail.get("lain2") : 0);
            totalSaldo = totalSaldo + (detail.get("sisa_piutang") != null ? (double) detail.get("sisa_piutang") : 0);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            int removeCollection = detail.get("removeOrder") != null ? (int) detail.get("removeOrder") : 0;
            String removeC = removeCollection > 0 ?"\u2022 " :"";

            cell = new PdfPCell(new Phrase(detail.get("no_customer") != null ? removeC + detail.get("no_customer").toString() : removeC + "", calibriRegular));//no cust
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("nama_outlet") != null ? detail.get("nama_outlet").toString() : "", calibriRegular));//name outlet
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            if (detail.get("tanggal") != null) {
                date = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_TYPE_15, detail.get("tanggal").toString());
            } else {
                date = "";
            }

            cell = new PdfPCell(new Phrase(date, calibriRegular));//tgl
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            int removeOrder = detail.get("removeCollection") != null ? (int) detail.get("removeCollection") : 0;
            String removeO= removeOrder > 0 ?"\u2022 " :"";

            cell = new PdfPCell(new Phrase(detail.get("no") != null ? removeO + detail.get("no").toString() : removeO + "", calibriRegular));//no tagihan
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("jumlah") != null ? format.format(detail.get("jumlah")) : "0", calibriRegular));//jumlah
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("no_order") != null ? detail.get("no_order").toString() : "", calibriRegular));//no faktur
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("nilai") != null ? format.format(detail.get("nilai")) : "0", calibriRegular));//nilai
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("tunai") != null ? format.format(detail.get("tunai")) : "0", calibriRegular));//tunai
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("giro") != null ? format.format(detail.get("giro")) : "0", calibriRegular));//giro
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("cheque") != null ? format.format(detail.get("cheque")) : "0", calibriRegular));//tf
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("transfer") != null ? format.format(detail.get("transfer")) : "0", calibriRegular));//lain2
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("lain2") != null ? format.format(detail.get("lain2")) : "0", calibriRegular));//saldo piutang
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("sisa_piutang") != null ? format.format(detail.get("sisa_piutang")) : "0", calibriRegular));//ket
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detail.get("keterangan") != null ? detail.get("keterangan").toString() : "", calibriRegular));//retur
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

        cell = new PdfPCell(new Phrase(format.format(totalCheque), calibriRegular));//retur
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
        Phrase text;

        for (int i = 0; i < 3; i++) {
            cell = new PdfPCell();
            text = new Phrase((""), calibriRegular);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.addElement(text);
            table.addCell(cell);
        }

        cell = new PdfPCell();
        text = new Phrase();
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

    public Boolean createPDF(File pdfFile, List<Map> lashList, String nameLash) {
        initFontPdf();
        Boolean isGood = false;
        user = SessionManagerQubes.getUserProfile();
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
            document.add(createPDFTop(nameLash));
            document.add(createPDFHeaderTable(lashList));
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

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}