package model;

public class Class_File extends Table {

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
}
