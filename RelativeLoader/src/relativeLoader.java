
import java.io.*;
import java.util.Scanner;
public class relativeLoader {

    private String pName; // da l esm eli l user hyktbo
    private String pLength;
    private String actualName; // da l esm bta3 l berngamg eli fi l file.. h3ml check eza kan da eli l user f3ln 3yzo wla l2.
    private Boolean nameFlag = false; // da 34an a4of eza kan l esm mktob s7 wla l2
    private String addr; //da l adress eli wa5do mn l file abl l t7wel
    private int intAdrr; // da 34an a7aawelo l hex 
    private boolean finish = false; // 34an a4of wslt le l E wla l2
    PrintWriter myPen = null; // 34an data fi file gded
    private String actualLength; // length l mwgod aslun fl file
    private int addrOfset;// l zeyada bta3t l address 
    private boolean enoughMemory = true;  // 34an a3ml check n l memory m4 akbr mn 32kilo byte.
    Scanner checkProgramLength ;
    boolean wrongLength = false;
    public relativeLoader() {
        
        try {
            myPen = new PrintWriter("MEMORY.TXT");
        } catch (FileNotFoundException ex) {
            System.out.println("Error in The PrintWriter");
        }
        readAssembler(); // l method eli by7sl feha kol 7aga
        if (enoughMemory&&wrongLength==false) {
            myPen.close();
        }
    }

    private void readAssembler() {
       
        Scanner programName = new Scanner(System.in);
        System.out.println("Enter The name Of the File");
        String progName = programName.nextLine();
        System.out.print("WHAT IS THE PROGRAM NAME: "); //bs2l 34an a3ml check 3aleh.
        Scanner input = new Scanner(System.in);
        pName = input.nextLine(); // pName feh l esm eli l user hyda5alo
        System.out.println("WHAT IS ITS LENGTH");

        pLength = input.nextLine(); //107A
        pLength = checkSizeOFHex(pLength,6); // 34an lw ktb 107A  we l mfrod yb2a 00107A . l method de htzwd l asfar .. 6 34an howa length we 3ddo 6

        File assembler = new File(progName+".txt"); //assembler da howa l file 
        Scanner readAssembler = null;
        try {
            readAssembler = new Scanner(assembler); // da eli mt5zn feh l file
        } catch (FileNotFoundException ex) {
            System.out.println("Error in readAssembler Scanner");
        }
        String read = null; // da eli hst3mlo 34an a2ra l file mn l readAssembler
        while (readAssembler.hasNext()) {

            read = readAssembler.next(); // read de h4of feha el 7gat eli et2rt.
            if (read.charAt(0) == 'H') { // lw awel 7rf et2ra bysawi H yb2a eli b3do howa esm l bernamg
                actualName = read.substring(1, 7); // b5li actual name = mn 1 le 6 esm l bernamg
                actualLength = read.substring(13, 19); // we nfs l mwdo3 le l lenghth
                if (actualName.compareTo(pName) == 0 && actualLength.compareTo(pLength) == 0) { // lw esm l bernamg howa l esm eli l user 3yzo yb2a tmam
                    System.out.println("Program Found");
                    nameFlag = true; // da flag 34an a4of lw ml2a4 l esm 5als
                    System.out.println("Enter The ofset of the start adress in decimal");
                    addrOfset = input.nextInt();
                    break; // b3ml break 34an y5rog mn l while
                }
            }
        }
        if (nameFlag == false) { // lw ml2a4 l esm
            System.out.println("Program Not Found");
        } else { // lw l2a l esm hykml
            System.out.println("\n\nMemory \t \t Data\n");
            myPen.println("\n\nMemory \t \t Data\n");
            // myPen.close();
            while (readAssembler.hasNext()) { // tol ma lsa fi 7aga m2renha4 fl readAssembler
                read = readAssembler.next(); // hn2raha b l read we nt3aml m3aha

                readT(read, readAssembler);// hna hnt3aml m3aha
            }
        }
    }
    
    private void readT(String read, Scanner readAssembler) {
        if (read.charAt(0) == 'T' && finish == false) { // lw awel 7rf mn l read howa T yb2a l gy l adress .. we b3ml check la akon d5lt fi bernamg gded
            addr = read.substring(1, 7); // ba5d l adress

            int temp = Integer.parseInt(addr, 16); // ba7awelo mn String hex le integer ex: 001000 > 4000 
            temp += addrOfset;

            addr = Integer.toHexString(temp); // we ba7awel mn l rqm l inter le hex tani : 4000 > 10000 , 34an am4i l asfar l zayada de
            String newT = bitMask(read.substring(9, 12), read);//ht3aml m3 l bit mask hna

            int i = 12; // h5li l i b 12 34an y3adi l length bta3 l T we yd5ol fi l instructions.
            try {
                while (true) { // tol ma howa true l mfrod lma l line bta3 l T y5ls y3ml exception fa da eli hy5leni a3rf eno 5ls
                    System.out.println(addr + "\t \t " + newT.substring(i, i + 2)); // hytb3 l address we l instructions rqmen rqmen
                    myPen.println(addr + "\t \t " + newT.substring(i, i + 2));

                    i = i + 2; // howa dlwa2t tb3 rqmen fa hzwd rqmen 34an ytb3 l gyen.
                    temp = Integer.parseInt(addr, 16); // h7awel l rqm mn string (hex) le int 34an a3rf azawedo.
                    temp++; // hzawedo b wa7d.
                    addr = Integer.toHexString(temp); //h7awelo le rqm hex String tani ex: 4010>100A
                }
            } catch (Exception ex) { // exception m3anaha N l Line 5ls.
                read = readAssembler.next();  // h2ral str l gded
                if (read.charAt(0) != 'E') { // lw awel 7rf m4 E
                    // System.out.println(read.charAt(0));
                    readT(read, readAssembler); // 3ed l funtion mn l awel. 34an y2ra l text l gdeda
                } else {
                    if (temp < 32768) {
                     int  addrtemp =  Integer.parseInt(addr,16);
                     String saddrTemp = Integer.toHexString(addrtemp);
                     int  actualAddrtemp =  Integer.parseInt(actualLength,16);
                     actualAddrtemp+=addrOfset;
                        String sactualAddrTemp = Integer.toHexString(actualAddrtemp);
                        if(saddrTemp.compareTo(sactualAddrTemp)==0){
                            finish = true;
                        }
                        else{
                            System.out.println("WRONGE LENGTH");
                            wrongLength = true;
                            finish = true;
                        }
                        // lw awel 7rf b E y3ni l bernamg 5ls .
                    } else {
                        System.out.println("No enough Memory");
                        System.out.println(addr+"heeeeeeeeeeeere");
                        enoughMemory = false;
                         finish = true;
                    }
                }
            }
        }
    }

    private String bitMask(String maskBits, String text) {
        String newT = null;

        int temp = Integer.parseInt(maskBits, 16);

        String bin = Integer.toBinaryString(temp);
        newT = text.substring(0, 14);
        for (int i = 0; i < bin.length(); i++) {

            if (bin.charAt(i) == '1') {
                try {

                    temp = Integer.parseInt(text.substring(14 + (6 * i), 18 + (6 * i)), 16);
                    temp += addrOfset;
                    String stringTemp = Integer.toHexString(temp);
                    stringTemp = checkSizeOFHex(stringTemp,4); // 34an lw l string kan 0001 hyb2a 1 bs .. fa hazwd 3 asfar .. false 34an howa addrs we 3ddo 4 bs

                    newT += stringTemp; // bzwd 3la l new text l 4kl l gded bt3ha b3d l t3del . bzwd l addr

                    newT += text.substring(18 + (6 * i), 20 + (6 * i)); // hna bzwd l opcode bta3 l instruction eli gaya

                } catch (Exception ex) {

                }
            } else if (bin.charAt(i) == '0') {
                try {
                    newT += text.substring(14 + (6 * i), 18 + (6 * i)); // b7ot fl newtext l 7gat heya heya bdon t3del 34an l bit mask feha kan b 0
                    newT += text.substring(18 + (6 * i), 20 + (6 * i)); // bzwd l op code .. 3mltha 3la mrten 34an lw kan l addr howa a5r addr mygeb4 error in mfe4 opcode y5do
                } catch (Exception ex) {

                }
            }
        }

        System.out.println(bin + " : BITMASK IN BINARY");
        System.out.println(text + " : T BEFORE MODIFICATION");
        System.out.println(newT + " : T AFTER MODIFICATION");
        return newT;

    }
    private String checkSizeOFHex(String stringTemp, int maxLength) {

        if (maxLength==4) {
            switch (stringTemp.length()) {
                case 3:
                    stringTemp = "0".concat(stringTemp);
                    break;
                case 2:
                    stringTemp = "00".concat(stringTemp);
                    break;
                case 1:
                    stringTemp = "000".concat(stringTemp);
                    break;
                case 0:
                    stringTemp = "0000".concat(stringTemp);
                    break;
                default:
                    break;
            }
        } else if (maxLength==6){
            switch (stringTemp.length()) {
                case 5:
                    stringTemp = "0".concat(stringTemp);
                    break;
                case 4:
                    stringTemp = "00".concat(stringTemp);
                    break;
                case 3:
                    stringTemp = "000".concat(stringTemp);
                    break;
                case 2:
                    stringTemp = "0000".concat(stringTemp);
                    break;
                case 1:
                    stringTemp = "00000".concat(stringTemp);
                    break;
                case 0:
                    stringTemp = "000000".concat(stringTemp);
                    break;
                default:
                    break;
            }
        }
        return stringTemp;
    }
    public static void main(String[] args) {
        relativeLoader loader = new relativeLoader();
    }
}
