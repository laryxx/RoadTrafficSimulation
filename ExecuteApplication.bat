@echo [off]
Start "" /wait python testdesign.py
Start "" /wait java -jar "Team Project.jar"
Start "" /wait python DisplayResults.py