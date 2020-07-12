package rendering.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenderTemplate 
{
    //This method is parsing the CSS and Javascript file content
    private static String getStaticFile(String fileName) throws Exception
    {
        /**  @param fileName --> file name of that file which needs to be parsed
         *   @return file content in string format
         * */

        Path path = Paths.get("").toAbsolutePath();
        File file = new File(path.toString()+"/static/" + fileName);

        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder fileContent = new StringBuilder();
        String temp;

        while ((temp = reader.readLine()) != null)
        {
            fileContent.append(temp);
        }
        
        return fileContent.toString();
    }

    private static String getParsedHtmlContent(BufferedReader reader) throws Exception
    {
        /**  @param reader --> BufferedReader's object to read the file
         *   @return file content in string format
         * */

        //This method is parsing the HTML file content
        String cssContent = null;
        String jsContent = null;

        StringBuilder htmlContent = new StringBuilder();
        String temp;


        boolean flag = true;

        while ((temp = reader.readLine()) != null)
        {
            //Checking if CSS file is present or not
            if(temp.contains("link rel=\"stylesheet\" ") && temp.contains("href="))
            {
                String[] css = temp.split(" ");

                for(String s : css)
                {
                    if(s.contains("href="))
                    {
                        int rIndex = s.lastIndexOf("\"");
                        int lIndex = s.indexOf("\"");
                        cssContent = getStaticFile(s.substring(lIndex+1,rIndex)); //Calling this method to get the
                                                                                    //parsed CSS file content
                    }
                }

                flag = false;
            }

            //Checking if Javascript file is present or not
            if(temp.contains("script src="))
            {
                String[] js = temp.split(" ");

                for(String s : js)
                {
                    if(s.contains("src="))
                    {
                        int rIndex = s.lastIndexOf("\"");
                        int lIndex = s.indexOf("\"");
                        jsContent = getStaticFile(s.substring(lIndex+1,rIndex));//Calling this method to get the
                                                                                   //parsed CSS file content
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


    public static String render(String fileName) throws Exception
    {
        /**  @param fileName --> file name of that file which needs to be parsed
         *   @return file content in string format
         * */


        //This method is rendering the HTML file content
        Path path = Paths.get("").toAbsolutePath();
        File file = new File(path.toString()+"/templates/" + fileName);

        BufferedReader reader = new BufferedReader(new FileReader(file));

        return getParsedHtmlContent(reader);
    }
}
