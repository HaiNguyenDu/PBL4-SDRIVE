@echo off
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /inheritance:r
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /remove "Everyone"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /remove "Everyone" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "Everyone":(OI)(CI)R
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "Everyone":(OI)(CI)R /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\hihi\fole\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
