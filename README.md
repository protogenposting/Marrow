# Marrow

A 2D animation software written in Java.

The animation tools are inspired by Godot's animation tools, where "channels" act as properties you can animate. So far, this includes:

....* position X
....* position Y
....* opacity
....* scale X
....* scale Y
....* shear X
....* shear Y
....* and rotation

Selecting a layer and adding a layer will add a "child layer," where all the keyframes from its "parent layer" will be applied to it.

To add a completely new layer free of all keyframes, add a layer to the very top "ParentLayer."

To actually play the animation, it must be both playing and started.

![alt text](https://github.com/protogenposting/Marrow/blob/main/marrow.png "Marrow Showcase")

## Installation

Launcher: https://github.com/protogenposting/Marrow-Launcher
The software comes with the launcher. Not always up to date with the repository.
