# Prompt Library

Use in Copilot Chat with # file picker:
  # PROMPT-01 + # story file → implement feature
  # PROMPT-02 + # story file → write unit tests
  # PROMPT-03 + # story file → write Selenium tests

Workflow:
1. PROMPT-01 backend → PROMPT-01 frontend
2. Close impl files → PROMPT-02 unit tests → mvn test
3. Close impl files → PROMPT-03 selenium → ./build.sh --with-selenium
4. All green → move story to done/
