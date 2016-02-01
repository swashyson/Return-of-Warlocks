
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Swashy
 */
public class Protocol {

    Path path;
    File file;
    FileOutputStream fileOut;

    public Protocol() {

        file = new File(new File(".").getAbsolutePath());
    }

    public String checkForCommands(String command) {
        String[] syntaxCommand = command.split(" ");
        if (syntaxCommand[0].equalsIgnoreCase("apa")) {

            try {
                String currentPath = file.getCanonicalPath();
                return currentPath;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }else if(syntaxCommand[0].equalsIgnoreCase("List")){
            try{
            String[] fileList = file.list();
            StringBuilder allFiles = new StringBuilder();
            System.out.println("Files in directory "+file.getCanonicalPath());
                allFiles.append("Files in directory "+file.getCanonicalPath());
                for(int i = 0 ;i<fileList.length; i++){
                
                  allFiles.append(fileList[i]+",");
                }
                  System.out.println(allFiles);
                return allFiles.toString();
            }catch (Exception ex) {
                ex.printStackTrace();
            }           
        }else if(syntaxCommand[0].equalsIgnoreCase("dl")){
            if(!syntaxCommand[1].isEmpty()){
                try{
                    String filePath =file.getCanonicalPath()+"\\"+syntaxCommand[1]; 

                    System.out.println("downloading "+filePath);
                    return filePath;
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else{
                return "Chosse a file";
            }
        }
        else if(syntaxCommand[0].equalsIgnoreCase("sup")){
            return syntaxCommand[1];
        }
        else{
            return "Command doesent exist";
        }
        return "";

    }
}
