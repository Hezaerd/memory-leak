# 🧠 Memory Leak

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.11-green?style=for-the-badge" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/Mod%20Loader-Fabric-blue?style=for-the-badge" alt="Fabric">
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="License">
</p>

A Fabric mod for **server administrators** to intentionally cause controlled memory leaks. Perfect for stress testing servers, testing memory monitoring tools, or simulating memory pressure scenarios.

## ⚠️ Warning

This mod is designed for **testing and development purposes only**. Using it on production servers without proper understanding can cause crashes and data loss.

## ✨ Features

- **Gradual Memory Leak** - Leak memory over a configurable time period
- **Boss Bar Progress** - Visual feedback showing leak progress (admin-only)
- **Full Control** - Start, stop, clear, and check status at any time
- **Permission-Based** - Only server operators can use leak commands

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) (0.18.4+)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download the latest release from [Modrinth](https://modrinth.com/mod/memory-leak)
4. Place the `.jar` file in your `mods` folder

## 🎮 Commands

All commands require **operator permissions** (level 4).

| Command | Description |
|---------|-------------|
| `/leak start <MB> <seconds>` | Start leaking `<MB>` megabytes over `<seconds>` seconds |
| `/leak stop` | Stop the current leak (keeps leaked memory) |
| `/leak clear` | Stop leak and free all leaked memory |
| `/leak status` | Check current leak status |

## 📜 License

This project is licensed under the [MIT License](LICENSE).

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/Hezaerd">Hezaerd</a>
</p>
