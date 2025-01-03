@echo off
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /inheritance:r
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /remove "Everyone"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /remove "Everyone" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "Everyone":(OI)(CI)M
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Everyone:(OI)(CI)M" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.1.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
