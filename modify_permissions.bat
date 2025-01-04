@echo off
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\folder\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\folder\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /inheritance:r
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /remove "Everyone"
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /remove "Everyone" /T
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /grant "Everyone":(OI)(CI)R
icacls "\\192.168.10.25\SDriver\Thanhan\folder\*" /grant "Everyone":(OI)(CI)R /T
