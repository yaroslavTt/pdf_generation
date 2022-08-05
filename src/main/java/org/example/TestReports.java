package org.example;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestReports {
    public static void main(String[] args) throws Exception {
        DataBean bean = DataBean.builder()
                .userName("Тиховецький Ярослав")
                .issuanceDate("03.08.2022")
                .build();

        BeanService service = new BeanService();

        service.saveToPdf(service.createJasperPrint(bean));
    }
}

class BeanService{
//    private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_needed_test.jrxml";
    private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_needed_test_24_size.jrxml";
//    private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_specific_test_24_size.jrxml";

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
        String exportPath = "C:\\Users\\yarik\\Desktop\\pdf_generation\\src\\main\\resources\\pdf-reports\\output\\template.pdf";
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