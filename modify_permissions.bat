@echo off
icacls "\\192.168.1.25\SDriver\Thanhan\hihi" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\hihi" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\hihi\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\hihi\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\hihi" /inheritance:r
icacls "\\192.168.1.25\SDriver\Thanhan\hihi" /remove "Everyone"
icacls "\\192.168.1.25\SDriver\Thanhan\hihi" /remove "Everyone" /T
icacls "\\192.168.1.25\SDriver\Thanhan\hihi" /grant "Everyone":(OI)(CI)R
icacls "\\192.168.1.25\SDriver\Thanhan\hihi\*" /grant "Everyone":(OI)(CI)R /T
