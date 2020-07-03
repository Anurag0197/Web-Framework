package rendering.template;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenderTemplate 
{
    private static String getStaticFile(String fileName) throws Exception
    {
        Path path = Paths.get("").toAbsolutePath();
        File file = new File(path.toString()+"/static/" + fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder fileContent = new StringBuilder();
        String temp;

        while ((temp = br.readLine()) != null)
        {
            fileContent.append(temp);
        }
        
        return fileContent.toString();
    }

    private static byte[] getImageFile(String fileName) throws Exception
    {
        Path path = Paths.get("").toAbsolutePath();
        File file = new File(path.toString()+"/media/" + fileName);

         BufferedImage i = ImageIO.read(file);
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         ImageIO.write(i, "jpg", byteArrayOutputStream);

         return byteArrayOutputStream.toByteArray();
    }

    public static String render(String fileName) throws Exception
    {
        String cssContent = null;
        String jsContent = null;
        byte[] imgContent;
        boolean flag = true;

        Path path = Paths.get("").toAbsolutePath();
        File file = new File(path.toString()+"/templates/" + fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder htmlContent = new StringBuilder();
        String temp;

        while ((temp = br.readLine()) != null)
        {
            if(temp.contains("link rel=\"stylesheet\" ") && temp.contains("href="))
            {
                String[] css = temp.split(" ");

                for(String s : css)
                {
                    if(s.contains("href="))
                    {
                        int rIndex = s.lastIndexOf("\"");
                        int lIndex = s.indexOf("\"");
                        cssContent = getStaticFile(s.substring(lIndex+1,rIndex));
                    }
                }

                flag = false;
            }

            if(temp.contains("script src="))
            {
                String[] js = temp.split(" ");

                for(String s : js)
                {
                    if(s.contains("src="))
                    {
                        int rIndex = s.lastIndexOf("\"");
                        int lIndex = s.indexOf("\"");
                        jsContent = getStaticFile(s.substring(lIndex+1,rIndex));
                    }
                }

                flag = false;
            }

            if(temp.contains("img src="))
            {
                String[] img = temp.split(" ");

                for(String s : img)
                {
                    if(s.contains("src="))
                    {
                        int rIndex = s.lastIndexOf("\"");
                        int lIndex = s.indexOf("\"");
                        imgContent = getImageFile(s.substring(lIndex+1,rIndex));
                   //     htmlContent.append(imgContent);
                    }
                }

                flag = false;
            }

            if(flag)
                htmlContent.append(temp);

            flag = true;
        }

        htmlContent.append("<style>").append(cssContent).append("</style>");
        htmlContent.append("<script>").append(jsContent).append("</script>");

        return htmlContent.toString();
    }
}
