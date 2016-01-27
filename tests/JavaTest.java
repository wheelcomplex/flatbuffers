/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.*;
import java.nio.ByteBuffer;
import MyGame.Example.*;
import NamespaceA.*;
import NamespaceA.NamespaceB.*;
import com.google.flatbuffers.FlatBufferBuilder;

class JavaTest {
    public static void main(String[] args) {

        // First, let's test reading a FlatBuffer generated by C++ code:
        // This file was generated from monsterdata_test.json

        byte[] data = null;
        File file = new File("monsterdata_test.mon");
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(file, "r");
            data = new byte[(int)f.length()];
            f.readFully(data);
            f.close();
        } catch(java.io.IOException e) {
            System.out.println("FlatBuffers test: couldn't read file");
            return;
        }

        // Now test it:

        ByteBuffer bb = ByteBuffer.wrap(data);
        TestBuffer(bb);

        // Second, let's create a FlatBuffer from scratch in Java, and test it also.
        // We use an initial size of 1 to exercise the reallocation algorithm,
        // normally a size larger than the typical FlatBuffer you generate would be
        // better for performance.
        FlatBufferBuilder fbb = new FlatBufferBuilder(1);

        // We set up the same values as monsterdata.json:

        int str = fbb.createString("MyMonster");

        int inv = Monster.createInventoryVector(fbb, new byte[] { 0, 1, 2, 3, 4 });

        int fred = fbb.createString("Fred");
        Monster.startMonster(fbb);
        Monster.addName(fbb, fred);
        int mon2 = Monster.endMonster(fbb);

        Monster.startTest4Vector(fbb, 2);
        Test.createTest(fbb, (short)10, (byte)20);
        Test.createTest(fbb, (short)30, (byte)40);
        int test4 = fbb.endVector();

        int testArrayOfString = Monster.createTestarrayofstringVector(fbb, new int[] {
            fbb.createString("test1"),
            fbb.createString("test2")
        });

        Monster.startMonster(fbb);
        Monster.addPos(fbb, Vec3.createVec3(fbb, 1.0f, 2.0f, 3.0f, 3.0,
                                                 Color.Green, (short)5, (byte)6));
        Monster.addHp(fbb, (short)80);
        Monster.addName(fbb, str);
        Monster.addInventory(fbb, inv);
        Monster.addTestType(fbb, (byte)Any.Monster);
        Monster.addTest(fbb, mon2);
        Monster.addTest4(fbb, test4);
        Monster.addTestarrayofstring(fbb, testArrayOfString);
        Monster.addTestbool(fbb, false);
        Monster.addTesthashu32Fnv1(fbb, Integer.MAX_VALUE + 1L);
        int mon = Monster.endMonster(fbb);

        Monster.finishMonsterBuffer(fbb, mon);

        // Write the result to a file for debugging purposes:
        // Note that the binaries are not necessarily identical, since the JSON
        // parser may serialize in a slightly different order than the above
        // Java code. They are functionally equivalent though.

        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream(
                                           "monsterdata_java_wire.mon"));
            os.write(fbb.dataBuffer().array(), fbb.dataBuffer().position(), fbb.offset());
            os.close();
        } catch(java.io.IOException e) {
            System.out.println("FlatBuffers test: couldn't write file");
            return;
        }

        // Test it:
        TestExtendedBuffer(fbb.dataBuffer());

        // Make sure it also works with read only ByteBuffers. This is slower,
        // since creating strings incurs an additional copy
        // (see Table.__string).
        TestExtendedBuffer(fbb.dataBuffer().asReadOnlyBuffer());

        TestEnums();

        //Attempt to mutate Monster fields and check whether the buffer has been mutated properly
        // revert to original values after testing
        Monster monster = Monster.getRootAsMonster(fbb.dataBuffer());

        // mana is optional and does not exist in the buffer so the mutation should fail
        // the mana field should retain its default value
        TestEq(monster.mutateMana((short)10), false);
        TestEq(monster.mana(), (short)150);

        // testType is an existing field and mutating it should succeed
        TestEq(monster.testType(), (byte)Any.Monster);
        TestEq(monster.mutateTestType(Any.NONE), true);
        TestEq(monster.testType(), (byte)Any.NONE);
        TestEq(monster.mutateTestType(Any.Monster), true);
        TestEq(monster.testType(), (byte)Any.Monster);

        //mutate the inventory vector
        TestEq(monster.mutateInventory(0, 1), true);
        TestEq(monster.mutateInventory(1, 2), true);
        TestEq(monster.mutateInventory(2, 3), true);
        TestEq(monster.mutateInventory(3, 4), true);
        TestEq(monster.mutateInventory(4, 5), true);

        for (int i = 0; i < monster.inventoryLength(); i++) {
            TestEq(monster.inventory(i), i + 1);
        }

        //reverse mutation
        TestEq(monster.mutateInventory(0, 0), true);
        TestEq(monster.mutateInventory(1, 1), true);
        TestEq(monster.mutateInventory(2, 2), true);
        TestEq(monster.mutateInventory(3, 3), true);
        TestEq(monster.mutateInventory(4, 4), true);

        // get a struct field and edit one of its fields
        Vec3 pos = monster.pos();
        TestEq(pos.x(), 1.0f);
        pos.mutateX(55.0f);
        TestEq(pos.x(), 55.0f);
        pos.mutateX(1.0f);
        TestEq(pos.x(), 1.0f);

        TestExtendedBuffer(fbb.dataBuffer().asReadOnlyBuffer());

        TestNamespaceNesting();

        TestNestedFlatBuffer();

        System.out.println("FlatBuffers test: completed successfully");
    }

    static void TestEnums() {
      TestEq(Color.name(Color.Red), "Red");
      TestEq(Color.name(Color.Blue), "Blue");
      TestEq(Any.name(Any.NONE), "NONE");
      TestEq(Any.name(Any.Monster), "Monster");
    }

    static void TestBuffer(ByteBuffer bb) {
        TestEq(Monster.MonsterBufferHasIdentifier(bb), true);
        
        Monster monster = Monster.getRootAsMonster(bb);

        TestEq(monster.hp(), (short)80);
        TestEq(monster.mana(), (short)150);  // default

        TestEq(monster.name(), "MyMonster");
        // monster.friendly() // can't access, deprecated

        Vec3 pos = monster.pos();
        TestEq(pos.x(), 1.0f);
        TestEq(pos.y(), 2.0f);
        TestEq(pos.z(), 3.0f);
        TestEq(pos.test1(), 3.0);
        TestEq(pos.test2(), Color.Green);
        Test t = pos.test3();
        TestEq(t.a(), (short)5);
        TestEq(t.b(), (byte)6);

        TestEq(monster.testType(), (byte)Any.Monster);
        Monster monster2 = new Monster();
        TestEq(monster.test(monster2) != null, true);
        TestEq(monster2.name(), "Fred");

        TestEq(monster.inventoryLength(), 5);
        int invsum = 0;
        for (int i = 0; i < monster.inventoryLength(); i++)
            invsum += monster.inventory(i);
        TestEq(invsum, 10);

        // Alternative way of accessing a vector:
        ByteBuffer ibb = monster.inventoryAsByteBuffer();
        invsum = 0;
        while (ibb.position() < ibb.limit())
            invsum += ibb.get();
        TestEq(invsum, 10);

        Test test_0 = monster.test4(0);
        Test test_1 = monster.test4(1);
        TestEq(monster.test4Length(), 2);
        TestEq(test_0.a() + test_0.b() + test_1.a() + test_1.b(), 100);

        TestEq(monster.testarrayofstringLength(), 2);
        TestEq(monster.testarrayofstring(0),"test1");
        TestEq(monster.testarrayofstring(1),"test2");

        TestEq(monster.testbool(), false);
    }

    // this method checks additional fields not present in the binary buffer read from file
    // these new tests are performed on top of the regular tests
    static void TestExtendedBuffer(ByteBuffer bb) {
        TestBuffer(bb);

        Monster monster = Monster.getRootAsMonster(bb);

        TestEq(monster.testhashu32Fnv1(), Integer.MAX_VALUE + 1L);
    }
    
    static void TestNamespaceNesting() {
        // reference / manipulate these to verify compilation
        FlatBufferBuilder fbb = new FlatBufferBuilder(1);
        
        TableInNestedNS.startTableInNestedNS(fbb);
        TableInNestedNS.addFoo(fbb, 1234);
        int nestedTableOff = TableInNestedNS.endTableInNestedNS(fbb);
        
        TableInFirstNS.startTableInFirstNS(fbb);      
        TableInFirstNS.addFooTable(fbb, nestedTableOff);
        int off = TableInFirstNS.endTableInFirstNS(fbb);
    }
    
    static void TestNestedFlatBuffer() {
        final String nestedMonsterName = "NestedMonsterName";
        final short nestedMonsterHp = 600;
        final short nestedMonsterMana = 1024;
        
        FlatBufferBuilder fbb1 = new FlatBufferBuilder(16);
        int str1 = fbb1.createString(nestedMonsterName);
        Monster.startMonster(fbb1);
        Monster.addName(fbb1, str1);
        Monster.addHp(fbb1, nestedMonsterHp);
        Monster.addMana(fbb1, nestedMonsterMana);
        int monster1 = Monster.endMonster(fbb1);
        Monster.finishMonsterBuffer(fbb1, monster1);
        byte[] fbb1Bytes = fbb1.sizedByteArray();
        fbb1 = null;
        
        FlatBufferBuilder fbb2 = new FlatBufferBuilder(16);        
        int str2 = fbb2.createString("My Monster");
        int nestedBuffer = Monster.createTestnestedflatbufferVector(fbb2, fbb1Bytes);
        Monster.startMonster(fbb2);
        Monster.addName(fbb2, str2);
        Monster.addHp(fbb2, (short)50);
        Monster.addMana(fbb2, (short)32);
        Monster.addTestnestedflatbuffer(fbb2, nestedBuffer);
        int monster = Monster.endMonster(fbb2);
        Monster.finishMonsterBuffer(fbb2, monster);
        
        // Now test the data extracted from the nested buffer
        Monster mons = Monster.getRootAsMonster(fbb2.dataBuffer());
        Monster nestedMonster = mons.testnestedflatbufferAsMonster();

        TestEq(nestedMonsterMana, nestedMonster.mana());
        TestEq(nestedMonsterHp, nestedMonster.hp());
        TestEq(nestedMonsterName, nestedMonster.name());
    }

    static <T> void TestEq(T a, T b) {
        if (!a.equals(b)) {
            System.out.println("" + a.getClass().getName() + " " + b.getClass().getName());
            System.out.println("FlatBuffers test FAILED: \'" + a + "\' != \'" + b + "\'");
            assert false;
            System.exit(1);
        }
    }
}
