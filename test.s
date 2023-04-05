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


