package org.example;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.extras.java8time.util.TemporalFormattingUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestReports {
    public static void main(String[] args) throws JRException, IOException, WriterException {
        DataBean bean = new DataBean("name", "Tykhovetskyi Yaroslav", "0" + LocalDate.now().getDayOfMonth() + ".08.2022");


        BeanService service = new BeanService();

        Map<EncodeHintType, ErrorCorrectionLevel> hashMap =
                new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRService.createQR("https://speak-ukrainian.org.ua",
                "/Users/aroslavtihoveckij/IdeaProjects/pdf_generation/src/main/resources/pdf-reports/output/demo.png",
                "UTF-8",
                hashMap,
                200,
                200);

        service.saveToPdf(service.createJasperPrint(bean));
    }
}

class BeanService{
    //        private static final String TEMPLATE_PATH = "/pdf-reports/half_a4_specific_font.jrxml";
//        private static final String TEMPLATE_PATH = "/pdf-reports/half_a4_needed_font.jrxml";
//        private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_specific_font_template.jrxml";
//        private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_needed_font_template.jrxml";
    private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_needed_test.jrxml";

    private Map<String, Object> getParameters(DataBean bean) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("BEAN", bean);
        return parameters;
    }

    public byte[] getPdfOutput(final DataBean bean){
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            JasperPrint jasperPrint = createJasperPrint(bean);
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public void saveToPdf(JasperPrint print) throws JRException {
        String exportPath = "/Users/aroslavtihoveckij/IdeaProjects/pdf_generation/src/main/resources/pdf-reports/output/template.pdf";
        JasperExportManager.exportReportToPdfFile(print, exportPath);
    }

    public JasperPrint createJasperPrint(DataBean bean) throws JRException, IOException {
        try (InputStream inputStream = new FileInputStream(getRealFilePath(TEMPLATE_PATH))) {
            final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            return JasperFillManager.fillReport(jasperReport, getParameters(bean), getDataSource(bean));
        }
    }

    private String getRealFilePath(final String path) throws IOException{
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(DataBean bean){
        return new JRBeanCollectionDataSource(Collections.singleton(bean));
    }
}