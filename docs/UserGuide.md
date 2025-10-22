# ModHero User Guide

_ModHero_ is a **CLI-first desktop app** for planning your university degree, designed to give you the speed and precision of command-line input while retaining the clarity of a graphical overview.  
It helps you build and adapt your 4-year course roadmap more efficiently than traditional spreadsheet or browser-based tools.

---

## 📘 Contents
- [Quick Start](#quick-start)
- [Features](#features)
    - [Viewing Help (`help`)](#viewing-help-help)
    - [Specifying Your Major (`major`)](#specifying-your-major-major)
    - [Adding an Elective (`elective`)](#adding-an-elective-elective)
    - [Deleting an Elective (`delete`)](#deleting-an-elective-delete)
    - [Generating a Recommended Schedule (`schedule`)](#generating-a-recommended-schedule-schedule)
    - [Clearing All Data (`clear`)](#clearing-all-data-clear)
    - [Exiting the Program (`exit`)](#exiting-the-program-exit)
    - [Saving Data](#saving-the-data)
    - [Loading and Editing Data Files](#loading-and-editing-data-files)
    - [Archiving Data Files *(coming in v2.0)*](#archiving-data-files-coming-in-v20)
- [FAQ](#faq)
- [Known Issues](#known-issues)
- [Command Summary](#command-summary)

---

## 🚀 Quick Start

1. Ensure you have **Java 17 or above** installed.
    - **Mac users:** Use the exact JDK version [prescribed here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [GitHub Releases](https://github.com/se-edu/addressbook-level3/releases).

3. Copy the file to your desired home folder for ModHero.

4. Open a command terminal and navigate to that folder:
   ```bash
   cd path/to/folder
   java -jar modhero.jar
   ```

5. A GUI should appear after a few seconds, with some sample data preloaded.

6. Type a command in the **command box** and press **Enter** to execute.

   **Examples:**
   ```bash
   help
   major CEG
   generate
   elective CS2113
   delete CS2113
   clear
   exit
   ```

7. Refer to the [Features](#features) section for full command details.

---

## ⚙️ Features

### Notes about command format
- Words in **UPPER_CASE** are parameters you must supply.  
  Example:
  ```
  elective MODULE_CODE
  → elective CS2113
  ```

- Square brackets `[ ]` denote optional fields.  
  Example:
  ```
  major MAJOR_NAME [specialisation SPECIALISATION]
  → major Computer Science specialisation AI
  ```

- Parameters followed by `...` can be repeated.  
  Example:
  ```
  elective MODULE_CODE...
  → elective CS2109S CS3230 CS3219
  ```

- Parameters can be entered in any order unless specified.
- Extra parameters for commands that take no input (e.g., `help`, `exit`) are ignored.  
  Example:
  ```
  help 123 → interpreted as help
  ```

---

### 🆘 Viewing Help: `help`

Shows a message explaining how to access the help page.

**Format:**
```
help
```

---

### 🎓 Specifying Your Major: `major`

Defines your primary degree major, which ModHero uses to load graduation requirements.

**Format:**
```
major MAJOR_NAME [specialisation SPECIALISATION] [minor MINOR_NAME]
```

**Examples:**
```
major Computer Science
major Information Security minor Statistics
major Computer Science specialisation Artificial Intelligence
major CS
```

---

### ➕ Adding an Elective: `elective`

Adds one or more electives to your plan.

**Format:**
```
elective MODULE_CODE ...
```

**Examples:**
```
elective CS2109S
elective CS2109S CS3230 CS3219
```

---

### ➖ Deleting an Elective: `delete`

Removes one or more electives from your plan.

**Format:**
```
delete MODULE_CODE ...
```

**Examples:**
```
delete CS2109S
```

---

### 🗓 Generating a Recommended Schedule: `schedule`

Generates a personalised 4-year study plan factoring in prerequisites, NUSMods availability, exchanges, and graduation requirements.

**Format:**
```
schedule
```

**Example:**
```
schedule
→ Returns recommended Year 1 and Year 2 modules, including core courses
```

---

### 🧹 Clearing All Data: `clear`

Deletes all modules and resets your plan.

**Format:**
```
clear
```

---

### 🚪 Exiting the Program: `exit`

Closes the program.

**Format:**
```
exit
```

---

### 💾 Saving the Data

All changes — adding electives, setting majors, generating schedules — are **saved automatically**.  
No manual saving is required.

---

### 🧰 Loading and Editing Data Files

ModHero stores your degree plan in:

```
[JAR file location]/data/modhero.json
```

- Advanced users can manually edit this JSON file if needed.
- ⚠️ **Caution:** Invalid edits may cause ModHero to reset your plan. Always back up before editing.

---

### 🗂 Archiving Data Files *(coming in v2.0)*

Future versions will allow archiving older study plans (e.g., SEP/NOC scenarios).

---

## ❓ FAQ

**Q:** How do I transfer my plan to another computer?  
**A:** Install ModHero on the new computer, then overwrite its default data file with your original `modhero.json`.

**Q:** Can I use ModHero offline?  
**A:** ModHero requires internet access to retrieve elective data from NUSMods.

---

## 🧭 Known Issues

- None reported.

---

## 📋 Command Summary

| Action   | Format / Examples |
|-----------|------------------|
| **Major** | `major MAJOR_NAME [specialisation SPECIALISATION] [minor MINOR_NAME]`<br>_e.g._ `major Computer Science specialisation Artificial Intelligence` |
| **Elective** | `elective MODULE_CODE ...`<br>_e.g._ `elective CS2109S CS2113` |
| **Delete** | `delete MODULE_CODE ...`<br>_e.g._ `delete CS2109S` |
| **Schedule** | `schedule` |
| **Clear** | `clear` |a
| **Help** | `help` |

---

© 2025 ModHero Team — CS2113-T10-4  
Based on SE-EDU framework.
