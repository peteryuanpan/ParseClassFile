package model;

import java.io.IOException;
import java.io.InputStream;

public class Attribute_BootstrapMethods extends Attribute_Info {

    private U2 num_bootstrap_methods;

    private Bootstrap_Method[] bootstrap_methods;

    public static Attribute_BootstrapMethods create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws IOException
    {
        Attribute_BootstrapMethods attr = new Attribute_BootstrapMethods();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.num_bootstrap_methods = U2.create(is);
        attr.bootstrap_methods = new Bootstrap_Method[attr.num_bootstrap_methods.getValue()];
        for (int i = 0; i < attr.num_bootstrap_methods.getValue(); i ++) {
            attr.bootstrap_methods[i] = Bootstrap_Method.create(is, constant_infos);
        }
        attr.newBytes();
        return attr;
    }
}
