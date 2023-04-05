# JRISC

Jrisc is a simple assembler for RISC-V written in Java.

Given a source file it generate the relative machine code.

At the moment are supported only the core instructions, so no pseudo-instructions are available.

The current implemented sets are :

- RV32I
- RV32M

__Build :__
```
javac -d BUILD SRC/*.java
```
__Run :__
```
java -cp "BUILD" Jrisc sourcefile outputfile
```

For example the following assembly :
```
;example source code
add     x0 x1 x2
sub     x0 x1 x2
sll     x0 x1 x2
slt     x0 x1 x2
sltu    x0 x1 x2
xor     x0 x1 x2
srl     x0 x1 x2
sra     x0 x1 x2
or      x0 x1 x2
and     x0 x1 x2

;note the position of the immediate
jalr   x0 x1 123
lb     x0 x1 123
lh     x0 x1 123
lw     x0 x1 123
lbu    x0 x1 123
lhu    x0 x1 123
addi   x0 x1 123
slti   x0 x1 123
sltiu  x0 x1 123
xori   x0 x1 123
ori    x0 x1 123
andi   x0 x1 123

ecall
ebreak
fence

slli   x0 x1 12
srli   x0 x1 12
srai   x0 x1 12

;note the position of the immediate
sb     x0 x1 123
sh     x0 x1 123
sw     x0 x1 123

label:
beq    x0 x1 label
bne    x0 x1 label
blt    x0 x1 label
bge    x0 x1 label
bltu   x0 x1 label
bgeu   x0 x1 label

jal    x0 label

lui    x0 123
auipc  x0 123
```
will generate the following machine code :
```
0x00208033
0x00208033
0x40208033
0x40208033
0x00209033
0x00209033
0x0020A033
0x0020A033
0x0020B033
0x0020B033
0x0020C033
0x0020C033
0x0020D033
0x0020D033
0x4020D033
0x4020D033
0x0020E033
0x0020E033
0x0020F033
0x0020F033
0x07B08067
0x07B08067
0x07B08003
0x07B08003
0x07B09003
0x07B09003
0x07B0A003
0x07B0A003
0x07B0C003
0x07B0C003
0x07B0D003
0x07B0D003
0x07B08013
0x07B08013
0x07B0A013
0x07B0A013
0x07B0B013
0x07B0B013
0x07B0C013
0x07B0C013
0x07B0E013
0x07B0E013
0x07B0F013
0x07B0F013
0x00000073
0x00000073
0x00100073
0x00100073
0x0000000F
0x0000000F
0x00C09013
0x00C09013
0x00C0D013
0x00C0D013
0x40C0D013
0x40C0D013
0x06008DA3
0x06008DA3
0x06009DA3
0x06009DA3
0x0600ADA3
0x0600ADA3
0x00100063
0x00100063
0xFE101EE3
0xFE101EE3
0xFE104CE3
0xFE104CE3
0xFE105AE3
0xFE105AE3
0xFE1068E3
0xFE1068E3
0xFE1076E3
0xFE1076E3
0xFE9FF06F
0xFE9FF06F
0x0007B037
0x0007B037
0x0007B017
0x0007B017
```