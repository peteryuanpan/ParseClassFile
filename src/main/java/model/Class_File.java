package model;

public class Class_File extends Table {

    public String class_file_path;

    public U4 magic;

    public U2 minor_version;

    public U2 major_version;

    public U2 constant_pool_count;

    public Constant_Info[] constant_infos;

    public Class_Access_Flag class_access_flag;

    public Class_Interface_Info this_class;

    public Class_Interface_Info super_class;

    public U2 interfaces_count;

    public Class_Interface_Info[] interfaces;

    public U2 fields_count;

    public Field_Info[] field_infos;

    public U2 methods_count;

    public Method_Info[] method_infos;

    public U2 attributes_count;

    public Attribute_Info[] attribute_infos;

    public void newBytes() {
        super.newBytes();
    }

    private StringBuilder sb;
    
    private void append(String s) {
        sb.append(s);
        sb.append("\n");
    }

    public String toString() {
        sb = new StringBuilder();
        append("--------Begin Parse Class File " + class_file_path + "--------");
        append("--------Begin Magic--------");
        append("magic: " + magic.parseBytesToHexString());
        append("--------End Magic--------");
        append("");
        append("--------Begin Version--------");
        append("minor_version: " + minor_version);
        append("major_version: " + major_version);
        append("--------End Version--------");
        append("");
        append("--------Begin Constant Pool--------");
        append("constant_pool_count: " + constant_pool_count);
        for (int i = 1; i < constant_pool_count.getValue(); i ++) {
            append("[" + i + "] " + constant_infos[i]);
        }
        append("--------End Constant Pool--------");
        append("");
        append("--------Begin Access Flag--------");
        append("[access_flag] " + class_access_flag);
        append("--------END Access Flag--------");
        append("");
        append("--------Begin Class & Interfaces--------");
        append("[this_class] " + this_class);
        append("");
        append("[super_class] " + super_class);
        append("");
        append("interfaces_count: " + interfaces_count);
        for (int i = 0; i < interfaces_count.getValue(); i ++) {
            Class_Interface_Info interface_ = interfaces[i];
            append("[" + i + "] " + interface_);
        }
        append("--------End Class & Interfaces--------");
        append("");
        append("--------Begin Fields--------");
        append("fields_count: " + fields_count);
        for (int i = 0; i < fields_count.getValue(); i ++) {
            append("[" + i + "] " + field_infos[i]);
        }
        append("--------End Fields--------");
        append("");
        append("--------Begin Methods--------");
        append("methods_count: " + methods_count);
        for (int i = 0; i < methods_count.getValue(); i ++) {
            append("[" + i + "] " + method_infos[i]);
        }
        append("--------End Methods--------");
        append("");
        append("--------Begin Attributes--------");
        append("attributes_count: " + attributes_count);
        for (int i = 0; i < attributes_count.getValue(); i ++) {
            append("[" + i + "] " + attribute_infos[i]);
        }
        append("--------End Attributes--------");
        append("");
        append("--------End Parse Class File " + class_file_path + "--------");
        return sb.toString();
    }
}
