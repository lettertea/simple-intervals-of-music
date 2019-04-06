import os

notes = []

for file in os.listdir('.'):
    if file.endswith(".py"):
        continue

    filename_w_ext = os.path.basename(file).lower()
    filename, file_extension = os.path.splitext(filename_w_ext)
    
    notes.append(filename.split("_"))


sortedNotes = sorted(notes, key=lambda note:int(note[2]))

for note in range(len(sortedNotes)):
    sortedNotes[note] = "_".join(sortedNotes[note])

print(sortedNotes)
