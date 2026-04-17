# Introduction and Core Philosophy

PydanticAI is a Python Agent Framework designed to make building applications with Large Language Models (LLMs) as type-safe and reliable as modern software development.

## 1. What is PydanticAI?
Developed by the team behind **Pydantic**, it is a framework that uses Pydantic models for every part of the AI interaction.
- **Typed Input**: Using `Deps` to inject validated external data.
- **Typed Output**: Using Pydantic models to enforce that LLMs return structured data.
- **Plain Old Python**: Unlike other frameworks that use complex, hidden abstractions (e.g., LangChain's chains), PydanticAI feels like standard Python code.

## 2. Core Philosophy: The "Typed Agent"
In PydanticAI, an agent is not just a black box. It is a strictly defined component.
- **Consistency**: The same Pydantic models used in your database or API are used to validate the AI's output.
- **IDE Support**: Full type hints mean your IDE can provide autocompletion and catch errors for agent inputs and outputs before you even run the code.
- **Maintenance**: Changes to your data models are automatically reflected in your AI prompts and validation logic.

## 3. Why chose PydanticAI over others?

| Feature | PydanticAI | LangChain | CrewAI |
| :--- | :--- | :--- | :--- |
| **Type Safety** | Built-in via Pydantic | Optional / Ad-hoc | Basic |
| **Logic** | Native Python | Custom DSL / Chains | Multi-agent focus |
| **Integration** | Seamless with FastAPI/Pydantic | Overwhelming ecosystem | Opinionated |
| **Debugging** | Standard Python debuggers | Custom LangSmith platform | Difficult |

## 4. The Agent Lifecycle
1.  **Request**: User sends a prompt to the agent (possibly with dependencies).
2.  **Model Interaction**: The agent formats the prompt, system prompt, tool schemas, and result schemas for the LLM.
3.  **Tool Calling**: If the LLM needs more info, it calls a tool, the result is added to the conversation, and the LLM continues.
4.  **Validation**: The final LLM response is parsed into the return type model. If it fails, the agent can optionally ask the LLM to fix it.
5.  **Result**: A validated, typed Python object is returned to the user.

> [!IMPORTANT]
> **Minimalism is Key**: 
> PydanticAI avoids the "Agentic loop" by default. It focuses on making individual agent runs predictable and easy to integrate into larger systems.

> [!TIP]
> **Production Ready**: 
> Because it is built on Pydantic, it is inherently ready for production deployment in data-intensive environments where structure is mandatory.
