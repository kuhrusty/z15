# Changelog

All notable changes to this project will be documented in this file.  The
format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/).

## Unreleased
#### Added
#### Changed
- [Issue #14](https://github.com/kuhrusty/z15/issues/14): add PNG
  versions of the vector graphics used for the background gradients in
  scenario titles, as pre-API-21 versions of Android don't seem to
  support them.
- [Issue #13](https://github.com/kuhrusty/z15/issues/13): improve
  enlarged map tile resolution on low-density displays.  Also draw the
  yellow highlighted-tile border inside the tile, instead of hanging
  half outside & getting cut off when drawn up against the edge of the
  view.


## [1.2] (Android versionCode 5) - 2019-06-16
#### Added
- Add `SetupScenarioActivity`, which displays the tiles to be set up and
  walks through them in numerical order.


## [1.1] (Android versionCode 3) - 2019-06-03
#### Changed
- In Scenario 9, fix a bug where only one card was being drawn per growl
  ([issue #12](https://github.com/kuhrusty/z15/issues/12)).


## [1.0] (Android versionCode 1) - 2019-05-31

This is the initial release; it handles the soundtrack & Zombie Deck,
but that's about all.
