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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class UnloadingPdfUtils {
    Font calibriRegularWhite, calibriRegular, calibriBoldUnderline, calibriUnderline, bigCalibriBold, bigCalibri, calibriBold, arialRegular, bigArialBold, arialBold, myriadproRegular;
    int MAX_COLUMN_TABLE = 5;
    int SIZE_FONT_NORMAL = 5;
    int SIZE_FONT_BIG = 7;
//    int MAX_WIDTH_JENIS_BARANG = 4;
//    int MAX_WIDTH_QTY = 7;
//    int MAX_WIDTH_BAIK = 8;
//    int MAX_WIDTH_RUSAK = 6;
//    int MAX_WIDTH_TERJUAL = 6;

    protected DecimalFormatSymbols otherSymbols;
    protected DecimalFormat format;
    private static UnloadingPdfUtils instance;
    private Context context;
    private static final String TAG = "UnloadingPdfUtils";
    private User user;

    public static UnloadingPdfUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UnloadingPdfUtils(context);
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

    public UnloadingPdfUtils(Context context) {//DeliverySummaryActivityEra
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
        arialRegular = new Font(loadArial(), SIZE_FONT_NORMAL);
        myriadproRegular = new Font(loadMyriad(), SIZE_FONT_NORMAL);
        bigArialBold = new Font(loadArialBold(), SIZE_FONT_BIG);
        arialBold = new Font(loadArialBold(), SIZE_FONT_NORMAL);//Font.BOLD

        calibriRegularWhite = new Font(loadCalibri(), SIZE_FONT_NORMAL, Color.WHITE);
        calibriRegular = new Font(loadCalibri(), SIZE_FONT_NORMAL);
        bigCalibriBold = new Font(loadCalibriBold(), SIZE_FONT_BIG);
        bigCalibri = new Font(loadCalibri(), SIZE_FONT_BIG);
        calibriBold = new Font(loadCalibriBold(), SIZE_FONT_NORMAL);//Font.BOLD

        calibriBoldUnderline = new Font(loadCalibriBold(), SIZE_FONT_BIG);
        calibriBoldUnderline.setStyle(Font.UNDERLINE);

        calibriUnderline = new Font(loadCalibri(), SIZE_FONT_BIG);
        calibriUnderline.setStyle(Font.UNDERLINE);
    }

    public void autoEnter(PdfPTable table, PdfPCell cell, int banyak) {
        for (int i = 0; i < banyak; i++) {
            cell = new PdfPCell(new Phrase(" ", calibriRegular));
            cell.setColspan(table.getNumberOfColumns());
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
    }

    public PdfPTable createPDFTitle(StockRequest header) {
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);
        PdfPCell cell;
        Phrase text;

        cell = new PdfPCell();
        text = new Phrase("", calibriRegular);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        table.addCell(cell);

        cell = new PdfPCell();
        text = new Phrase("", calibriRegular);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        table.addCell(cell);

        cell = new PdfPCell();
        text = new Phrase("", calibriRegular);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(text);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Adm. Penjualan", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Surat Jalan", calibriBoldUnderline));
        cell.setUseAscender(true);
        cell.setColspan(MAX_COLUMN_TABLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        String noSJ = Helper.isEmpty(header.getNo_surat_jalan(), "");
        cell = new PdfPCell(new Phrase("No.SJ : " + noSJ, calibriUnderline));
        cell.setUseAscender(true);
        cell.setColspan(MAX_COLUMN_TABLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable createPDFTop(StockRequest header) {
        PdfPCell cell;

        PdfPTable mainTable = new PdfPTable(MAX_COLUMN_TABLE);
        mainTable.setWidthPercentage(100);

        cell = new PdfPCell(new Phrase("PT. ASIASEJAHTERA PERDANA PHARMACEUTICAL", bigCalibri));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Phrase("", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(cell);

        PdfPTable rightTable1 = new PdfPTable(2);
        rightTable1.setWidthPercentage(100);
        rightTable1.setWidths(new int[]{15, 85});

        cell = new PdfPCell(new Phrase("Tgl SJ", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        rightTable1.addCell(cell);

        String tglKirim;
        if (!Helper.isNullOrEmpty(header.getTanggal_kirim())) {
            tglKirim = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getTanggal_kirim());
        } else {
            tglKirim = "";
        }
        cell = new PdfPCell(new Phrase(": " + tglKirim, bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        rightTable1.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(rightTable1);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Phrase(user.getUsername() + " (" + user.getFull_name() + ")", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Phrase("", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(cell);

        PdfPTable rightTable2 = new PdfPTable(2);
        rightTable2.setWidthPercentage(100);
        rightTable2.setWidths(new int[]{15, 85});

        cell = new PdfPCell(new Phrase("No. Order", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        rightTable2.addCell(cell);

        String noOrder = Helper.isEmpty(header.getNo_doc(), "");
        cell = new PdfPCell(new Phrase(": " + noOrder, bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        rightTable2.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(rightTable2);
        mainTable.addCell(cell);

        cell = new PdfPCell(new Phrase("", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(3);
        mainTable.addCell(cell);

        PdfPTable rightTable3 = new PdfPTable(2);
        rightTable3.setWidthPercentage(100);
        rightTable3.setWidths(new int[]{15, 85});

        cell = new PdfPCell(new Phrase("Tgl. Order", bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        rightTable3.addCell(cell);

        String tgl;
        if (!Helper.isNullOrEmpty(header.getReq_date())) {
            tgl = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getReq_date());
        } else {
            tgl = "";
        }
        cell = new PdfPCell(new Phrase(": " + tgl, bigCalibri));
        cell.setBorder(Rectangle.NO_BORDER);
        rightTable3.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.addElement(rightTable3);
        mainTable.addCell(cell);

        return mainTable;
    }

    public PdfPTable createPDFSign() {
        PdfPCell cell;
        Phrase text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);

        //ttd
        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Diterima Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Dikembalikan Oleh", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //1
        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        //2
        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        //3
        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        cell.setPadding(5);
        table.addCell(cell);

        //4
        cell = new PdfPCell(new Phrase("", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Gudang", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);


        cell = new PdfPCell(new Phrase(user.getUsername() + " (" + user.getFull_name() + ")", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        cell.setPadding(5);
        table.addCell(cell);

        return table;
    }

    public PdfPTable createPDFHeaderTable(List<Material> materialList) {
        setFormatSeparator();
        PdfPCell cell;
        Phrase text;
        PdfPTable table = new PdfPTable(MAX_COLUMN_TABLE);
        table.setWidthPercentage(100);

        autoEnter(table, new PdfPCell(), 1);

        cell = new PdfPCell(new Phrase("Jenis Barang", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Banyaknya", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sisa", calibriRegular));
        cell.setUseAscender(true);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Terjual", calibriRegular));
        cell.setUseAscender(true);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Baik", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("B.S/Rusak/Kurang", calibriRegular));
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        table.addCell(cell);

        //detail
        for (int i = 0; i < materialList.size(); i++) {
            String nameMat = Helper.isEmpty(materialList.get(i).getNama(), "");
            cell = new PdfPCell(new Phrase(nameMat, calibriRegular));//jenis barang
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            String uom = Helper.isEmpty(materialList.get(i).getUom(), "");
            String qty = format.format(materialList.get(i).getQty());
            cell = new PdfPCell(new Phrase(qty + " " + uom, calibriRegular));//qty
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            String uomSisa = Helper.isEmpty(materialList.get(i).getUomSisa(), "");
            String qtySisa = format.format(materialList.get(i).getQtySisa());
            cell = new PdfPCell(new Phrase(qtySisa + " " + uomSisa, calibriRegular));//baik
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

//            cell = new PdfPCell(new Phrase(qtySisa + " " + uomSisa, calibriRegular));//rusak
            cell = new PdfPCell(new Phrase("", calibriRegular));//rusak
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setPadding(5);
            table.addCell(cell);

            String qtyOrder = format.format(materialList.get(i).getQty() - materialList.get(i).getQtySisa());
            cell = new PdfPCell(new Phrase(qtyOrder + " " + uom, calibriRegular));//terjual
            cell.setUseAscender(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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

    public Boolean createPDF(File pdfFile, StockRequest header, List<Material> mList) {
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
            user = SessionManagerQubes.getUserProfile();

            document.add(createPDFTitle(header));
            document.add(createPDFTop(header));
            document.add(createPDFHeaderTable(mList));
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