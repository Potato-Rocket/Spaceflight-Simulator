# Spaceflight-Simulator

A Java application to simulate 3D gravitational physics. Currently, this project is in early stages of development.

Javadoc tool should function to generate explanation of the code.

## Configuration Setup

This program allows both the graphical parameters and the initial conditions of the simulation to be specified in user configuration files.

### Directory Structure

The simulator requires configuration files to be placed in a specific location:

1. Create a configuration directory:
   - **Linux/macOS**: Create a folder named `solarsystemsim` inside your `~/.config/` directory:
     ```
     mkdir -p ~/.config/solarsystemsim
     ```
   - **Windows**: Create a folder named `solarsystemsim` inside your user home directory's `.config` folder:
     ```
     mkdir %USERPROFILE%\.config\solarsystemsim
     ```

### Required Configuration Files

You need two main configuration files:

1. **setup.properties**: Contains general simulator settings
2. **system.properties**: (or any other name specified in setup.properties) - Defines celestial bodies and their properties

### Setting Up Your Configuration

1. Copy the example configuration files from the project directory:
   ```
   cp example_setup.properties ~/.config/solarsystemsim/setup.properties
   cp example_system.properties ~/.config/solarsystemsim/system.properties
   ```
2. Edit these files to customize the simulation. Note that the simulation assumes that a consistent set of units is used throughout the system setup file. For example, if using kg-m-s, be sure to give every value in terms of these.

## Usage

When you start the program, the view will automatically encompass every one of your bodies. To rotate the view, click and drag on the screen. To zoom in or out, you can use the scroll wheel. Alternatively, these keybindings can be used:

- W: Tilt view up
- S: Tilt view down
- A: Rotate view left
- D: Rotate view right


- J: Zoom in
- K: Zoom out

To increase or decrease the virtual time-scale, use the following keybindings:

- Q: Slow down time
- E: Speed up time

To toggle the center of the view between different bodies, use these keybindings:

- H: Go to previous body
- L: Go to next body

Finally, these keybinding can be used for other functions:

- F1: Reset viewing angle and zoom
- F2: Toggle whether to view planet sizes to scale with distance or on a log scale
- F3: Toggle whether to show extra info on screen
