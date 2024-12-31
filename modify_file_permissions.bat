@echo off
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /grant "PBL4\XPhuc:F"
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /inheritance:r
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /grant "PBL4\XPhuc:F"
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /remove "Everyone"
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /inheritance:d
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /grant "PBL4\Administrator:F"
icacls "\\192.168.10.25\SDriver\XPhuc\newdoc.docx" /grant "PBL4\XPhuc:F"
