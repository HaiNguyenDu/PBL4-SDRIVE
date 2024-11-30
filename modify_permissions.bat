@echo off
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /inheritance:r
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /remove "PBL4\Phuc"
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /remove "PBL4\Phuc" /T
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /grant "PBL4\Phuc":(OI)(CI)R
icacls "\\192.168.10.25\SDriver\Thanhan\folder\*" /grant "PBL4\Phuc":(OI)(CI)R /T
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\folder" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\folder\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\folder\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
