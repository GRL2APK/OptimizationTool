import numpy as np
import matplotlib.pyplot as plt
import math
from mpl_toolkits import mplot3d
import pyodbc

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
filteredops = []
cursor = conn.cursor()
cursor.execute('select op1, op2 from TblOrderedPairs where status=1')
orderedpairResult = cursor.fetchall()

for row in orderedpairResult:
    if (row[0] in filteredops):
        print("exists")
        if (row[1] in filteredops):
            print("exists")
        else:
            filteredops.append(row[1])
    elif (row[1] in filteredops):
        filteredops.append(row[0])
    else:
        filteredops.append(row[1])
        filteredops.append(row[0])

# for o in filteredops:
#     print(o)

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
            # print(NFRList[j])
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
    NFRPriority.remove(minprio)
    opsNFRlist.remove(ops)
    i = i - 1
    i = i + 1


for o in range(len(NFRList)):
    orderedNFRList.append(NFRList[o])
    orderedPriority.append(NFRPriority[o])
    orderedOpsNFRList.append(opsNFRlist[o])
    #print(opsNFRlist[o])


i = 0
modorderedOpsNFRList = []
while i < len(orderedNFRList):
    #print(orderedNFRList[i])
    #print(orderedPriority[i])
    modoplist = []
    opslist = orderedOpsNFRList[i]
    for o in opslist:
        if (o in filteredops):
            modoplist.append(o)
    modorderedOpsNFRList.append(modoplist)
    i = i + 1
    #print(modoplist)

i = 0
while i < len(orderedNFRList):
    #print(orderedNFRList[i])
    #print(orderedPriority[i])
    #print("Hello")
    print(modorderedOpsNFRList[i])

    i = i + 1

orderedOpsNFRList = modorderedOpsNFRList
print("=========================")
colorarraylist = []
colorarraylist.append('#ffa200')
colorarraylist.append('#0066cc')
colorarraylist.append('#b30047')
colorarraylist.append('#0bda9c')
colorarraylist.append('#ffd500')

colorarray = []
i = 0
z = 5
operationalizationList = []
opcoordinates = []
while i < len(orderedNFRList):
    #print(orderedNFRList[i])
    #print(orderedPriority[i])
    #print(orderedOpsNFRList[i])
    xpoints = []
    ypoints = []
    coordinate = []
    coordinates0 = []
    ops = []
    ops = orderedOpsNFRList[i]
    col = colorarraylist[i]
    print("Color "+col)
    colorarray.append(col)
    no_of_points = len(ops)
    #no_of_points = 15 testing circle
    print("Number of points "+str(no_of_points))
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
        ax.scatter(x, y, z, s=30)
        ax.plot(xpoints, ypoints, z, '--', c='red')

    z = z - 2
    i = i + 1
status = []

for o in range(len(operationalizationList)):
    print(operationalizationList[o])
    coord = []
    coord = opcoordinates[o]
    status.append(0)
    print(coord)


cursor = conn.cursor()
cursor.execute('select op1, op2 from TblOrderedPairs where status=1')
orderedpairResult = cursor.fetchall()

# for row in orderedpairResult:
#     if (row[0] in filteredops):
#         print("exists")
#         if (row[1] in filteredops):
#             print("exists")
#         else:
#             filteredops.append(row[1])
#     elif (row[1] in filteredops):
#         filteredops.append(row[0])
#     else:
#         filteredops.append(row[1])
#         filteredops.append(row[0])
#
# for o in filteredops:
#     print(o)



# for row in orderedpairResult:
#     if (row[0] in operationalizationList):
#
#         index_pos1 = operationalizationList.index(row[0], 0)
#         print(row[0] + " Exists at "+str(index_pos1))
#         status [index_pos1] = 1
#     if (row[1] in operationalizationList):
#         index_pos1 = operationalizationList.index(row[1], 0)
#         print(row[1] + " Exists at " + str(index_pos1))
#         status[index_pos1] = 1
#
cursor = conn.cursor()
cursor.execute('select op1, op2 from TblOrderedPairs where status=1')
orderedpairResult = cursor.fetchall()


colindex = 0
for row in orderedpairResult:
    o1 = row[0]
    o2 = row[1]
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

plt.show()
