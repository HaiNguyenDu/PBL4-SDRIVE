@echo off
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /inheritance:r
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /remove "PBL4\Phuc"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /remove "PBL4\Phuc" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Phuc":(OI)(CI)M
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Phuc:(OI)(CI)M" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
