# 📝 NoteIt - Minimalist Notes App

A modern, lightweight Android notes application built to demonstrate the latest Jetpack Compose ecosystem, including the experimental Navigation 3 framework.

## ✨ Features

- Full CRUD Support: Effortlessly create, view, and manage your notes.

- Advanced Search: Filter through your notes with optional Case-Sensitivity toggling.

- Bulk Selection Mode: Select multiple notes at once to delete them in a single batch.

- Note Limit: Intelligently restricted to 10 notes maximum (to keep things organized!).

- Local Persistence: Powered by Room Database for offline reliability.

## 🛠 Tech Stack

This project explores a modern Android architecture:

- Jetpack Compose: Declarative UI implementation.

- Navigation 3 (androidx.navigation3): Implementing the latest approach to screen transitions and backstack management.

- Room Persistence Library: Local database management with Flow support for real-time UI updates.

- Dagger-Hilt: Dependency injection for clean and testable code.

- State Management: Using ViewModel and mutableStateOf for a reactive and performant UI.

## 🚀 Key Learning Points

This project was built to master specific modern Android concepts:

1. State-driven Navigation: Managing the backstack manually using SnapshotStateList as per Navigation 3's new philosophy.

2. Reactive Search Logic: Implementing real-time filtering that balances UI performance with database queries.

3. Hilt Integration: Providing NoteDao and ViewModel dependencies in a clean, modular way.

## 📸 Screenshots

<img width="1920" height="1080" alt="Screenshot (29)" src="https://github.com/user-attachments/assets/09f79e06-161e-495f-9616-405981b30079" />
<img width="1920" height="1080" alt="Screenshot (30)" src="https://github.com/user-attachments/assets/1054b04b-6110-491b-a925-e92fcb081184" />
<img width="1920" height="1080" alt="Screenshot (31)" src="https://github.com/user-attachments/assets/46a6e601-1e4f-4cba-b650-cd3adc5749ab" />
<img width="1920" height="1080" alt="Screenshot (32)" src="https://github.com/user-attachments/assets/72f6f832-ada9-4a1b-8b02-e25ab03f1daa" />
<img width="1920" height="1080" alt="Screenshot (33)" src="https://github.com/user-attachments/assets/d29f4efa-7a9a-464f-8356-9d921be6aea6" />

## 🔧 Installation

1. Clone this repository :- ` git clone https://github.com/your-username/TutorialRun.git `

2. Open the project in Android Studio (Ladybug or later recommended).

3. Build and run on your device or emulator.
