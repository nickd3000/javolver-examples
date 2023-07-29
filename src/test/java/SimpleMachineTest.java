import com.physmo.javolverexamples.programming.simplemachinie.SimpleMachine2;
import org.junit.Test;

public class SimpleMachineTest {

    @Test
    public void testLDBetweenRegisters() {
        SimpleMachine2 sm = new SimpleMachine2();
        sm.regA=0;
        sm.regB=5;
        sm.memory[0]=sm.getMicrocode().getOpcodeFromName("LD A,B");
        System.out.println("regA:"+sm.regA);
        sm.runCycle();
        sm.runCycle();
        sm.runCycle();

        System.out.println("regA:"+sm.regA);
    }

    @Test
    public void testLoadingValueFromMemoryAddressToRegister() {
        SimpleMachine2 sm = new SimpleMachine2();
        sm.regA=0;
        sm.memory[0]=sm.getMicrocode().getOpcodeFromName("LD A,pBYTE");
        sm.memory[1]=20;
        sm.memory[20]=123;
        System.out.println("regA:"+sm.regA);
        sm.runCycle();
        sm.runCycle();
        sm.runCycle();

        System.out.println("regA:"+sm.regA);
    }
//"LD pBYTE,A"

    @Test
    public void testStoringRegisterValueInAddress() {
        SimpleMachine2 sm = new SimpleMachine2();
        sm.regA=123;
        sm.memory[0]=sm.getMicrocode().getOpcodeFromName("LD pBYTE,A");
        sm.memory[1]=20;
        sm.memory[20]=0;
        System.out.println("memory[20]:"+sm.memory[20]);
        sm.runCycle();
        sm.runCycle();
        sm.runCycle();
        System.out.println("memory[20]:"+sm.memory[20]);
    }

    @Test
    public void testAdd() {
        SimpleMachine2 sm = new SimpleMachine2();
        sm.regA=2;
        sm.regB=3;
        sm.memory[0]=sm.getMicrocode().getOpcodeFromName("ADD A,B");
        System.out.println("regA:"+sm.regA);
        sm.runCycle();
        System.out.println("regA:"+sm.regA);
    }
}
