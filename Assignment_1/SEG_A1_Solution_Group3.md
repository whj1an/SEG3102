# Assignment 1
**Group 3**
Haojian Wang 300411829, Like Xiao

## Question 1
The core domain of the AEMS is **Schedule Compliance** System.
For the Subdomains: 

|            Subdomain            |    Type    |
| :-----------------------------: | :--------: |
|       Schedule Generation       |  Generic   |
|             Payroll             |  Generic   |
|    Authentication / Identity    |  Generic   |
|     Scheduling Preferences      | Supporting |
| Employee Information Management | Supporting |
|          Organization           | Supporting |

## Question 2
### Assumptions
1. Notifying the departments's manager (which shows at Step 7) is a side effect handled outside of the domain model, so it is listed as a Note rather than as a post
2. Every employee is assigned to EXACTLY one department.
3. An employee is uniquely identified by SIN, and registering an employee whose SIN already exists is not allowed
4. `employeeInfo` should include Address and Banking Information, which are created together with the employee
5. Shifts are created before hand by the department-manager.
6. An employee can only select upcoming shifts of their own department.
7. when an employee submits a new selection, it replaces their previous selection for the same upcoming shifts.
8. Any `...Info` arguments are data transfer objects.

### Application Commands

|           USE CAES           | STEP | Command                                                           | Domain0significant                 |
| :--------------------------: | :--: | ----------------------------------------------------------------- | ---------------------------------- |
|      Select Work Shifts      |  1   | `getUpcomingShifts(employeeId)`                                   | NO(I think it should be read-only) |
|      Select Work Shifts      |  3   | `selectShifts(employeeId, shiftIds)`                              | YES                                |
| Register Department Employee |  3   | `registerEmployee(managerId, employeeInfo)`                       | Yes                                |
| Register Department Employee |  5   | `getDepartments()`                                                | No                                 |
| Register Department Employee |  6   | `assignEmployeeToDepartment(managerId, employeeId, departmentId)` | Yes                                |

**1. Contracts**
`registerEmployee(managerId, emplyeeInfo`

**Preconditions**
- signed-in airport manager (existing airport manager identified by managerId)
- employeeInfo does not match an existing Employee (e.g employee has unique SIN)

**Postconditions**
- a new Employee EMP was created
- EMP was initialized from employeeInfo
- a new id number was assigned to emp
- a new Address addr was created and initialized from the address information in employeeInfo
- addr was set as the address of emp
- a new Banking Information bank was created and initialized from the banking information in employeeInfo
- bank was set as the banking information of EMP.

**2. Assign Employee to Department**
`assignEmplyeeToDepartment(managerId, EmployeeId, departmentId)`

- signed-in airport manager (existing airport manager identified by managerId)
- there is an existing employee identified by employeeId
- there is an existing department identified by departmentId
- the employee is not already assigned to a department?
*EMP was added to the employee's department*

**3. Select Shifts**
`selectShifts(employeeId, ShiftIds)`

if preference is an object.
Then the previous Shift Preferences of the employee were removed (instance destruction, association destruction)

for each shift s identified in shiftIds: 
- a new Shift Preference pref was created (instance creation)
- pref was linked to the employee (association creation)
- pref was linked to s (association creation)

## Question 3
I choose 'Select Work Shifts'.

Reason is that the Shifts system won't be changed easily. A Shift defined by its date, start time, and end time, and an employee seen through their availability and shifts.