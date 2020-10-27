import numpy as np
import matplotlib.pyplot as plt
import math
from mpl_toolkits import mplot3d
from matplotlib.pyplot import cm
import pyodbc
import itertools
def PointsInCircum(r,n=25):
    return [(math.cos(2*pi/n*x)*r,math.sin(2*pi/n*x)*r) for x in range(0,n+1)]




conn = pyodbc.connect('Driver={SQL Server};'
                      'Server=DESKTOP-TPTM0G5;'
                      'Database=OptimizationDB;'
                      'Trusted_Connection=yes;')
pi = math.pi

fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')


ax.set_zlabel('No. of Levels')
ax.set_yticklabels([])
ax.set_xticklabels([])


cursor = conn.cursor()
cursor.execute('select distinct NFRName from Tblgivennfrdetails')
result = cursor.fetchall()
NFRList = []
NFRPriority = []
for row in result:
    NFRList.append(row[0])
    #print(row[0])

opsNFRlist = []
for nfr in NFRList:
    oplist = []
    sql = "select distinct priority from Tblgivennfrdetails where NFRName = (?)"
    nfrName = (str(nfr),)
    cursor = conn.cursor()
    cursor.execute(sql, nfrName)
    myresult = cursor.fetchall()

    for row in myresult:
        NFRPriority.append(row[0])
    sql2 = "select  Operationalization from Tblgivennfrdetails where NFRName = (?)"
    cursor = conn.cursor()
    cursor.execute(sql2, nfrName)
    res = cursor.fetchall()

    for row in res:
        oplist.append(row[0])
    opsNFRlist.append(oplist)


# initialize color array
originaltablenamelist = []
colorarrylist = []
cursor = conn.cursor()
cursor.execute('select tablename NFRName from Tbltablenamelist')
tablenameresult = cursor.fetchall()
color = iter(cm.rainbow(np.linspace(0,4.2,25)))
# color=iter(cm.rainbow(np.linspace(0,1,5)))
# color = itertools.cycle(["r", "b", "g"])
for row in tablenameresult:
    #print(row[0])
    tabname = row[0]
    c = next(color)
    colorarrylist.append(c)
    originaltablenamelist.append(tabname)

print("colors are ========")
for c in colorarrylist:
    print(c)

orderedNFRList = []
orderedPriority = []
orderedOpsNFRList = []
i = 0
k = 0
while i < len(NFRList):
    j = 0
    minprio = 10
    index = 10
    ops = []
    nfrname = ""
    while j < len(NFRList):
        if minprio > NFRPriority[j]:
            index = j
            minprio = NFRPriority[j]
            #print(NFRList[j])
            # print("minprio "+str(minprio))
            # print("index "+str(index))
            ops = opsNFRlist[j]
            nfrname = NFRList[j]
        j = j + 1

    if minprio != 10:
        orderedNFRList.append(nfrname)

        orderedPriority.append(minprio)
        orderedOpsNFRList.append(ops)
        k = k + 1
    #
    NFRList.remove(nfrname)
    print("removing "+nfrname)
    NFRPriority.remove(minprio)
    opsNFRlist.remove(ops)
    i = i - 1
    i = i + 1

for o in range(len(NFRList)):
    print("Remaining "+NFRList[o])
    orderedNFRList.append(NFRList[o])
    orderedPriority.append(NFRPriority[o])
    orderedOpsNFRList.append(opsNFRlist[o])



colorarray = []
i = 0
z = 12
operationalizationList = []
opcoordinates = []
while i < len(orderedNFRList):
    print(orderedNFRList[i])
    print(orderedPriority[i])
    print(orderedOpsNFRList[i])
    xpoints = []
    ypoints = []
    coordinate = []
    coordinates0 = []
    ops = []
    ops = orderedOpsNFRList[i]


    no_of_points = len(ops)
    #no_of_points = 15 testing circle
    #print("Number of points "+str(no_of_points))
    PointsList = PointsInCircum(100, no_of_points)
    #print(PointsList)
    ik = 0
    for j in PointsList:
        x = j[0]
        y = j[1]
        op = []
        op = orderedOpsNFRList[i]
        coordinate = [x, y, z]
        coordinates0.append(coordinate)
        if ik < no_of_points:
            #print(op[ik])
            operationalizationList.append(op[ik])
            opcoordinates.append(coordinate)



        ik = ik + 1

        xpoints.append(x)
        ypoints.append(y)
        print("x = " + str(x) + " y = " + str(y))
        ax.scatter(x, y, z)
        ax.plot(xpoints, ypoints, z, '--', c='red')

    z = z - 2
    i = i + 1

for o in range(len(operationalizationList)):
    print(operationalizationList[o])
    coord = []
    coord = opcoordinates[o]
    print(coord)

cursor = conn.cursor()
cursor.execute('select op1, op2, nfr1, nfr2 from TblOrderedPairs')
orderedpairResult = cursor.fetchall()


colindex = 0
for row in orderedpairResult:
    o1 = row[0]
    o2 = row[1]
    nfr1 = row[2]
    nfr2 = row[3]
    subnfr1 = nfr1[:2]
    subnfr2 = nfr2[:2]
    subnfr = subnfr1+subnfr2
    print("substring of nfrs "+subnfr)
    index_col = originaltablenamelist.index(subnfr,0)
    col = colorarrylist[index_col]
    print("Color...")
    #print(col)
    index_pos1 = operationalizationList.index(o1, 0)
    #print("Index of "+o1+" is "+str(index_pos1))
    index_pos2 = operationalizationList.index(o2, 0)
    #print("Index of " + o2 + " is " + str(index_pos2))
    coord1 = opcoordinates[index_pos1]
    coord2 = opcoordinates[index_pos2]
    x0 = coord1[0]
    y0 = coord1[1]
    z0 = coord1[2]

    x1 = coord2[0]
    y1 = coord2[1]
    z1 = coord2[2]
    #col = colorarray[colindex]
    ax.plot([x0, x1], [y0, y1], [z0, z1], c='#a8a396')
    colindex = colindex + 1



tablenamelist = []
cursor = conn.cursor()
cursor.execute('select tablename NFRName from Tbltablenamelist')
tablenameresult = cursor.fetchall()
for row in tablenameresult:
    #print(row[0])
    tabname = row[0]
    tabname = tabname + "_w1removed"
    tablenamelist.append(tabname)


for tabname in tablenamelist:
    print(tabname)
    cursor = conn.cursor()
    cursor.execute('select op1, op2 from '+tabname)
    w1removedlist = cursor.fetchall()
    for row in w1removedlist:
        #print(row[0]+" "+row[1])
        cursor1 = conn.cursor()
        cursor1.execute("update TblOrderedPairs set status=1 where op1='"+row[0]+"' and op2='"+row[1]+"'")
        conn.commit()

#
# cursor = conn.cursor()
# cursor.execute('select op1, op2 from TblOrderedPairs where status=1')
# orderedpairResult = cursor.fetchall()
#
#
# colindex = 0
# for row in orderedpairResult:
#     o1 = row[0]
#     o2 = row[1]
#     index_pos1 = operationalizationList.index(o1, 0)
#     #print("Index of "+o1+" is "+str(index_pos1))
#     index_pos2 = operationalizationList.index(o2, 0)
#     #print("Index of " + o2 + " is " + str(index_pos2))
#     coord1 = opcoordinates[index_pos1]
#     coord2 = opcoordinates[index_pos2]
#     x0 = coord1[0]
#     y0 = coord1[1]
#     z0 = coord1[2]
#
#     x1 = coord2[0]
#     y1 = coord2[1]
#     z1 = coord2[2]
#     #col = colorarray[colindex]
#     ax.plot([x0, x1], [y0, y1], [z0, z1], c='black')

plt.show()
