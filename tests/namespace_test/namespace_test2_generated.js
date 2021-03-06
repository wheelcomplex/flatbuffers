// automatically generated by the FlatBuffers compiler, do not modify

/**
 * @const
*/
var NamespaceA = NamespaceA || {};

/**
 * @const
*/
NamespaceA.NamespaceB = NamespaceA.NamespaceB || {};

/**
 * @constructor
 */
NamespaceA.TableInFirstNS = function() {
  /**
   * @type {flatbuffers.ByteBuffer}
   */
  this.bb = null;

  /**
   * @type {number}
   */
  this.bb_pos = 0;
};

/**
 * @param {number} i
 * @param {flatbuffers.ByteBuffer} bb
 * @returns {NamespaceA.TableInFirstNS}
 */
NamespaceA.TableInFirstNS.prototype.__init = function(i, bb) {
  this.bb_pos = i;
  this.bb = bb;
  return this;
};

/**
 * @param {flatbuffers.ByteBuffer} bb
 * @param {NamespaceA.TableInFirstNS=} obj
 * @returns {NamespaceA.TableInFirstNS}
 */
NamespaceA.TableInFirstNS.getRootAsTableInFirstNS = function(bb, obj) {
  return (obj || new NamespaceA.TableInFirstNS).__init(bb.readInt32(bb.position()) + bb.position(), bb);
};

/**
 * @param {NamespaceA.NamespaceB.TableInNestedNS=} obj
 * @returns {NamespaceA.NamespaceB.TableInNestedNS}
 */
NamespaceA.TableInFirstNS.prototype.fooTable = function(obj) {
  var offset = this.bb.__offset(this.bb_pos, 4);
  return offset ? (obj || new NamespaceA.NamespaceB.TableInNestedNS).__init(this.bb.__indirect(this.bb_pos + offset), this.bb) : null;
};

/**
 * @returns {NamespaceA.NamespaceB.EnumInNestedNS}
 */
NamespaceA.TableInFirstNS.prototype.fooEnum = function() {
  var offset = this.bb.__offset(this.bb_pos, 6);
  return offset ? /** @type {NamespaceA.NamespaceB.EnumInNestedNS} */ (this.bb.readInt8(this.bb_pos + offset)) : NamespaceA.NamespaceB.EnumInNestedNS.A;
};

/**
 * @param {NamespaceA.NamespaceB.StructInNestedNS=} obj
 * @returns {NamespaceA.NamespaceB.StructInNestedNS}
 */
NamespaceA.TableInFirstNS.prototype.fooStruct = function(obj) {
  var offset = this.bb.__offset(this.bb_pos, 8);
  return offset ? (obj || new NamespaceA.NamespaceB.StructInNestedNS).__init(this.bb_pos + offset, this.bb) : null;
};

/**
 * @param {flatbuffers.Builder} builder
 */
NamespaceA.TableInFirstNS.startTableInFirstNS = function(builder) {
  builder.startObject(3);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} fooTableOffset
 */
NamespaceA.TableInFirstNS.addFooTable = function(builder, fooTableOffset) {
  builder.addFieldOffset(0, fooTableOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {NamespaceA.NamespaceB.EnumInNestedNS} fooEnum
 */
NamespaceA.TableInFirstNS.addFooEnum = function(builder, fooEnum) {
  builder.addFieldInt8(1, fooEnum, NamespaceA.NamespaceB.EnumInNestedNS.A);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} fooStructOffset
 */
NamespaceA.TableInFirstNS.addFooStruct = function(builder, fooStructOffset) {
  builder.addFieldStruct(2, fooStructOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @returns {flatbuffers.Offset}
 */
NamespaceA.TableInFirstNS.endTableInFirstNS = function(builder) {
  var offset = builder.endObject();
  return offset;
};

// Exports for Node.js and RequireJS
this.NamespaceA = NamespaceA;
