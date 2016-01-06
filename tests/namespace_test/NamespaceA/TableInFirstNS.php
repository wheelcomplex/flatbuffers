<?php
// automatically generated, do not modify

namespace NamespaceA;

use \Google\FlatBuffers\Struct;
use \Google\FlatBuffers\Table;
use \Google\FlatBuffers\ByteBuffer;
use \Google\FlatBuffers\FlatBufferBuilder;

class TableInFirstNS extends Table
{
    /**
     * @param ByteBuffer $bb
     * @return TableInFirstNS
     */
    public static function getRootAsTableInFirstNS(ByteBuffer $bb)
    {
        $obj = new TableInFirstNS();
        return ($obj->init($bb->getInt($bb->getPosition()) + $bb->getPosition(), $bb));
    }

    /**
     * @param int $_i offset
     * @param ByteBuffer $_bb
     * @return TableInFirstNS
     **/
    public function init($_i, ByteBuffer $_bb)
    {
        $this->bb_pos = $_i;
        $this->bb = $_bb;
        return $this;
    }

    public function getFooTable()
    {
        $obj = new TableInNestedNS();
        $o = $this->__offset(4);
        return $o != 0 ? $obj->init($o + $this->bb_pos, $this->bb) : 0;
    }

    /**
     * @return sbyte
     */
    public function getFooEnum()
    {
        $o = $this->__offset(6);
        return $o != 0 ? $this->bb->getSbyte($o + $this->bb_pos) : \NamespaceA\NamespaceB\EnumInNestedNS::A;
    }

    public function getFooStruct()
    {
        $obj = new StructInNestedNS();
        $o = $this->__offset(8);
        return $o != 0 ? $obj->init($o + $this->bb_pos, $this->bb) : 0;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return void
     */
    public static function startTableInFirstNS(FlatBufferBuilder $builder)
    {
        $builder->StartObject(3);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return TableInFirstNS
     */
    public static function createTableInFirstNS(FlatBufferBuilder $builder, $foo_table, $foo_enum, $foo_struct)
    {
        $builder->startObject(3);
        self::addFooTable($builder, $foo_table);
        self::addFooEnum($builder, $foo_enum);
        self::addFooStruct($builder, $foo_struct);
        $o = $builder->endObject();
        return $o;
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addFooTable(FlatBufferBuilder $builder, $fooTable)
    {
        $builder->addOffsetX(0, $fooTable, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param sbyte
     * @return void
     */
    public static function addFooEnum(FlatBufferBuilder $builder, $fooEnum)
    {
        $builder->addSbyteX(1, $fooEnum, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @param int
     * @return void
     */
    public static function addFooStruct(FlatBufferBuilder $builder, $fooStruct)
    {
        $builder->addStructX(2, $fooStruct, 0);
    }

    /**
     * @param FlatBufferBuilder $builder
     * @return int table offset
     */
    public static function endTableInFirstNS(FlatBufferBuilder $builder)
    {
        $o = $builder->endObject();
        return $o;
    }
}
