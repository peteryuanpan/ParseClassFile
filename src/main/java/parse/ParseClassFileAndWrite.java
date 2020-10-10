package parse;

import constant.Constants;
import model.Class_File;

import java.io.FileWriter;

public class ParseClassFileAndWrite {

    static void parse_write(String class_file_path) {
        try {
            Class_File class_file = ParseClassFile.parse(class_file_path);
            System.out.println(class_file);

            String file_name = class_file_path.substring(class_file_path.lastIndexOf('\\') + 1);
            file_name = "Result" + file_name.replaceAll(".class", ".txt");
            String result_class_file_path = Constants.RESOURCE_PATH + file_name;
            System.out.println("--------Write class_file to File " + result_class_file_path + "--------");

            FileWriter myWriter = new FileWriter(result_class_file_path);
            myWriter.write(class_file.toString());
            myWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parse_write(Constants.INTEGER_NUMBER_DEMO);
    }
}
