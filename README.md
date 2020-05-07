# AvicusNetwork
A **massive** maven project of all of the Avicus Network's required plugin suite in one convenient maven project.

Required server software: [SportPaper (w/ Custom modifications, please use this link)](https://github.com/VectorMC/SportPaper), and [Travertine](https://github.com/VectorMC/Travertine)

Required plugins: The Via Plugins, ProtocolLib, LibsDisguises, WorldEdit, LuckPerms, and NuVotifier

# Plugins:

## Atlas
Runs matches, parses XML, and generates docs.
* Core
  * The main plugin. This holds the general parsing infrastructure for maps and running matches. 
  * This module holds the functionality which loads the rest of the external jars.
* Competitive-Objectives
  * Holds parsing and data classes for all objectives related to competitive.
* Docs-Generator
  * Holds code for generating the docs.avicus.net documentation files from the documentation in each file.
* Arcade
  * Holds modules for the arcade gamemodes.
* Walls
  * Holds the walls module and special kits.

## Atrio
This is the lobby plugin. It handles basic lobby things such as portals and jump pads.

## Hook
* Core
  * This is used to be where all the DB stuff was, but then Magma came along. It's kinda useless.
* Discord
  * The legacy discord bot. This runs user registration and the such. This is slowly being moved to the ruby app.
* TeamSpeak
  * The teamspeak bot. Handles user registration and ranks. Useless as of 12/7/17.
* Bukkit
  * Bukkit plugin which handles database stuff. This is becoming more and more obsolete as things are being moved to Magma. 
  * This is compiled after Atlas, so it allows us to use Atlas things. This runs on servers without Atlas, however, so checks should be made to ensure Atlas before including classes.

## Magma
* Core
  * Core API and database connections live here. This runs on every server bukkit/bungee.
* Bungee
  * The bungee plugin. Handles user registration and MOTDs.
*  Bukkit
  * Core code for most of the stuff we do. Code ranges from API to backpacks and gadgets.

## Mars
The tournament/scrimmage plugin. This runs all of the tournament and scrimmage things.


## Licenses
Not all projects here are licensed the same, therefore the following are the licenses for each piece of software in this project:
* MIT License
  * Modules originally included in this project
    * Atlas
    * Atrio
    * Hook
    * Magma
    * Mars
  * Modules not originally included in this project, but were also licensed by the same author (The Avicus Network, 2018)
    * Compendium
    * RequirePlugins
  * Modules/Projects not originally included in this project, but were licensed by another party
    * Author: Keenan Thompson, 2018
      * Tabbed
    * Author: Tracker Developers, 2012-2013
      * Tracker
* Multiple Licenses
  * Modules not originally included in this project, but were licensed by another party, though the license for the source code they developed is the GNU General Public License v3
    * sk89q-command-framework
* No license specified/found
  * Grave
  * Quest
  * Tracker
  * Tutorial
  * Bossy

A copy of the original licenses can be found in the file LICENSES.txt, and the original licenses were left in their folders to be clear.
