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
import java.util.Locale;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class OverLkPdfUtils {
    Font calibriRegularWhite, calibriBoldUnderline, calibriRegular, bigCalibriBold, bigCalibri, calibriBold, arialRegular, bigArialBold, arialBold, myriadproRegular;
    int SIZE_FONT_NORMAL = 5;
    int SIZE_FONT_BIG = 8;
    int MAX_COLUMN_TABLE = 18;

    String volumeUnit = null, weightUnit = null;
    BigDecimal totalVolume = BigDecimal.ZERO, totalWeight = BigDecimal.ZERO;
    BigDecimal totalVolumeFU = BigDecimal.ZERO, totalWeightFU = BigDecimal.ZERO;

    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private static OverLkPdfUtils instance;
    private Context context;
    private static final String TAG = "LashPdfUtils";

    public static OverLkPdfUtils getInstance(Context context) {
        if (instance == null) {
            instance = new OverLkPdfUtils(context);
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

    public OverLkPdfUtils(Context context) {//DeliverySummaryActivityEra
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
        bigCalibriBold.setStyle(Font.BOLD);
        bigCalibri = new Font(loadCalibri(), SIZE_FONT_BIG);
        bigArialBold = new Font(loadArialBold(), SIZE_FONT_BIG);
        calibriBold = new Font(loadCalibriBold(), SIZE_FONT_NORMAL);//Font.BOLD
        arialBold = new Font(loadArialBold(), SIZE_FONT_NORMAL);//Font.BOLD

        calibriBoldUnderline = new Font(loadCalibriBold(), SIZE_FONT_BIG);
        calibriBoldUnderline.setStyle(Font.UNDERLINE | Font.BOLD);
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

        cell = new PdfPCell(new Phrase("USULAN PENGIRIMAN DENGAN KONDIIS OVER LK/MULTI BON/OVER TOP", calibriBoldUnderline));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);

        autoEnter(table,cell,2);

        return table;
    }

    public PdfPTable createPDFTop() {
        PdfPCell cell;
        Phrase text;

        PdfPTable mainTable = new PdfPTable(MAX_COLUMN_TABLE);
        mainTable.setWidthPercentage(100);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("ASPP : BOTABEK", bigCalibriBold);
        cell.setColspan(15);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        text = new Phrase("TGL : " + Helper.getTodayDate(Constants.DATE_FORMAT_4), bigCalibriBold);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        text = new Phrase("DEPO : TANGERANG", bigCalibriBold);
        cell.setColspan(15);
        cell.addElement(text);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        text = new Phrase("", bigCalibri);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(text);
        mainTable.addCell(cell);

        return mainTable;
    }

    public PdfPTable createPDFSign() {
        PdfPCell cell;
        Phrase text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);
//        table.setWidths(new int[]{MAX_WIDTH_NO, MAX_WIDTH_CUSTOMER, MAX_WIDTH_OUTLET,
//                MAX_WIDTH_TGL, MAX_WIDTH_NO_TAGIHAN, MAX_WIDTH_JMLH,
//                MAX_WIDTH_NO_FAKTUR, MAX_WIDTH_NILAI, MAX_WIDTH_TUNAI,
//                MAX_WIDTH_GIRO, MAX_WIDTH_TF, MAX_WIDTH_RETUR,
//                MAX_WIDTH_LAIN, MAX_WIDTH_SALDO_PIUTANG, MAX_WIDTH_KET});

        //ttd
        cell = new PdfPCell(new Phrase("DSM/SPV", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DALTA SPV", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RM", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DALTA MGR", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("DIR of GT DP", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("COO", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PRESDIR", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //line

        for (int i = 0; i < 3; i++) {
            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(3);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(3);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(3);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(2);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(2);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(3);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("", calibriRegular));
            cell.setColspan(3);
            cell.setUseAscender(true);
            if (i == 2) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setPadding(5);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase("TT & NAMA JELAS :", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TT & NAMA JELAS :", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TT & NAMA JELAS :", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TT & NAMA JELAS :", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
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

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                cell = new PdfPCell(new Phrase("SUSANTO W.", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);

            if (i == 2) {
                cell = new PdfPCell(new Phrase("MARTHA", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setColspan(3);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);

            if (i == 2) {
                cell = new PdfPCell(new Phrase("ANJAS", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setColspan(3);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);

            if (i == 2) {
                cell = new PdfPCell(new Phrase("YUSUS", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setColspan(2);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);

            if (i == 2) {
                cell = new PdfPCell(new Phrase("NOVIANDY", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setColspan(2);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);

            if (i == 2) {
                cell = new PdfPCell(new Phrase("HENKY BENYAMIN", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setColspan(3);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);

            if (i == 2) {
                cell = new PdfPCell(new Phrase("ENDI DJOJONEGORO", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            } else {
                cell = new PdfPCell(new Phrase("", calibriRegular));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            }
            cell.setColspan(3);
            cell.setUseAscender(true);
            cell.setPadding(5);
            table.addCell(cell);
        }

        autoEnter(table, cell, 1);

        cell = new PdfPCell(new Phrase("NOTE: \nOVER LK 15.000.000-50.000.000 (RM & DALTA MGR & DIR of GT DP)\n" +
                "OVER LK 50.000.000-100.000.000 (RM & DALTA MGR & DIR of GT DP & COO)\n" +
                "OVER LK 100.000.000 UP (RM & DALTA MGR & DIR of GT DP & COO & PRESDIR)", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(10);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        autoEnter(table, cell, 3);

        return table;
    }

    public PdfPTable createPDFHeaderTable() {
        setFormatSeparator();
        PdfPCell cell;
        Phrase text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);
//        table.setWidths(new int[]{MAX_WIDTH_NO, MAX_WIDTH_CUSTOMER, MAX_WIDTH_OUTLET,
//                MAX_WIDTH_TGL, MAX_WIDTH_NO_TAGIHAN, MAX_WIDTH_JMLH,
//                MAX_WIDTH_NO_FAKTUR, MAX_WIDTH_NILAI, MAX_WIDTH_TUNAI,
//                MAX_WIDTH_GIRO, MAX_WIDTH_TF, MAX_WIDTH_RETUR,
//                MAX_WIDTH_LAIN, MAX_WIDTH_SALDO_PIUTANG, MAX_WIDTH_KET});

        autoEnter(table, new PdfPCell(), 1);

        cell = new PdfPCell(new Phrase("NAMA SR", calibriRegular));
        cell.setBorder(Rectangle.BOX);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NO. CUST", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NAMA TOKO", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("ALAMAT", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("LK", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("CATATAN ADMINISTRASI", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("POSISI PIUTANG SAAT INI", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("USULAN DSM/RM", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RATA2/BLN", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PEMBAYARAN", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TGL. FAKTUR", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PRODUK (KRT)", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TGL JTH TEMPO", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NILAI PIUTANG (RUPIAH)", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("BG / TGL JT BG", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TGL KIRIM", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("JML BRG (CRT)", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NILAI RP", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("KONDISI (NOTA/BG)", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("OVER LK (RP)", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("QTY", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RP", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //detail

        for (int i = 0; i < 6; i++) {
            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), calibriRegular));//no
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);
        }

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

    private void setFormatSeparator() {
        otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        format = new DecimalFormat("#,###,###,###.###", otherSymbols);
        format.setDecimalSeparatorAlwaysShown(false);
    }
}