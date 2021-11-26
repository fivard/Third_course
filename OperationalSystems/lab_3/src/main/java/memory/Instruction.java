package memory;

import java.util.Vector;

public class Instruction {
    public String inst;
    public Vector<Integer> addresses;

    public Instruction(String inst, Vector<Integer> addresses) {
        this.inst = inst;
        this.addresses = addresses;
    }

    public String addressesToString(){
        String addrs = "";
        for (int i = 0; i < Kernel.countAddresses; i++)
            addrs += addresses.elementAt(i) + ", ";
        return addrs;
    }

}
