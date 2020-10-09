package model;

import java.io.IOException;
import java.io.InputStream;

public class Bootstrap_Method extends Attribute_Extend {

    private U2 bootstrap_method_ref;

    private U2 num_bootstrap_arguments;

    private Bootstrap_Argument[] bootstrap_arguments;

    public static Bootstrap_Method create(InputStream is, Constant_Info[] constant_infos) throws IOException {
        Bootstrap_Method bm = new Bootstrap_Method();
        bm.bootstrap_method_ref = U2.create(is);
        bm.num_bootstrap_arguments = U2.create(is);
        bm.bootstrap_arguments = new Bootstrap_Argument[bm.num_bootstrap_arguments.getValue()];
        for (int i = 0; i < bm.num_bootstrap_arguments.getValue(); i ++) {
            bm.bootstrap_arguments[i] = Bootstrap_Argument.create(is, constant_infos);
        }
        bm.newBytes();
        return bm;
    }
}
