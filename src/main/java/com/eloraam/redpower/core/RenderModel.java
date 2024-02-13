//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\Minecraft-Deobfuscator3000-master\1.7.10 stable mappings"!

//Decompiled by Procyon!

package com.eloraam.redpower.core;

import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.client.resources.*;
import java.util.*;
import java.io.*;

public class RenderModel
{
    public Vector3[] vertices;
    public TexVertex[][] texs;
    int[][][] groups;
    
    public static RenderModel loadModel(final String location) {
        try {
            final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location));
            final InputStream is = resource.getInputStream();
            final ModelReader ml = new ModelReader();
            ml.readModel(is);
            final List<TexVertex[]> vtl = new ArrayList<TexVertex[]>();
            int i = 0;
            while (i < ml.faceno.size()) {
                final TexVertex[] tr = new TexVertex[4];
                for (int lgs = 0; lgs < 4; ++lgs) {
                    final int lgmn = ml.faceno.get(i);
                    ++i;
                    if (lgmn < 0) {
                        throw new IllegalArgumentException("Non-Quad Face");
                    }
                    final int lgsn = ml.faceno.get(i);
                    ++i;
                    final TexVertex t = ml.texvert.get(lgsn - 1).copy();
                    t.vtx = lgmn - 1;
                    t.v = 1.0 - t.v;
                    tr[lgs] = t;
                }
                int lgs = ml.faceno.get(i);
                ++i;
                if (lgs >= 0) {
                    throw new IllegalArgumentException("Non-Quad Face");
                }
                vtl.add(tr);
            }
            final RenderModel model = new RenderModel();
            model.vertices = ml.vertex.toArray(new Vector3[0]);
            model.texs = vtl.toArray(new TexVertex[0][]);
            model.groups = new int[ml.grcnt.size()][][];
            for (i = 0; i < ml.grcnt.size(); ++i) {
                final int lgs = ml.grcnt.get(i);
                model.groups[i] = new int[lgs][];
                for (int lgmn = 0; lgmn < ml.grcnt.get(i); ++lgmn) {
                    model.groups[i][lgmn] = new int[2];
                }
            }
            i = 0;
            int lgs = -1;
            int lgmn = -1;
            int lgsn = -1;
            while (i < ml.groups.size()) {
                if (lgs >= 0) {
                    model.groups[lgmn][lgsn][0] = lgs;
                    model.groups[lgmn][lgsn][1] = ml.groups.get(i + 2);
                }
                lgmn = ml.groups.get(i);
                lgsn = ml.groups.get(i + 1);
                lgs = ml.groups.get(i + 2);
                i += 3;
            }
            if (lgs >= 0) {
                model.groups[lgmn][lgsn][0] = lgs;
                model.groups[lgmn][lgsn][1] = ml.fno;
            }
            return model;
        }
        catch (IOException exc) {
            exc.printStackTrace();
            return null;
        }
    }
    
    public RenderModel scale(final double factor) {
        for (final Vector3 vertex : this.vertices) {
            vertex.multiply(factor);
        }
        return this;
    }
    
    public static class ModelReader
    {
        public List<Vector3> vertex;
        public List<Integer> faceno;
        public List<TexVertex> texvert;
        public List<Integer> groups;
        public List<Integer> grcnt;
        int fno;
        
        public ModelReader() {
            this.vertex = new ArrayList<Vector3>();
            this.faceno = new ArrayList<Integer>();
            this.texvert = new ArrayList<TexVertex>();
            this.groups = new ArrayList<Integer>();
            this.grcnt = new ArrayList<Integer>();
            this.fno = 0;
        }
        
        private void eatLine(final StreamTokenizer tok) throws IOException {
            while (tok.nextToken() != -1) {
                if (tok.ttype != 10) {
                    continue;
                }
            }
        }
        
        private void endLine(final StreamTokenizer tok) throws IOException {
            if (tok.nextToken() != 10) {
                throw new IllegalArgumentException("Parse error");
            }
        }
        
        private double getFloat(final StreamTokenizer tok) throws IOException {
            if (tok.nextToken() != -2) {
                throw new IllegalArgumentException("Parse error");
            }
            return tok.nval;
        }
        
        private int getInt(final StreamTokenizer tok) throws IOException {
            if (tok.nextToken() != -2) {
                throw new IllegalArgumentException("Parse error");
            }
            return (int)tok.nval;
        }
        
        private void parseFace(final StreamTokenizer tok) throws IOException {
            while (true) {
                tok.nextToken();
                if (tok.ttype == -1 || tok.ttype == 10) {
                    this.faceno.add(-1);
                    ++this.fno;
                    return;
                }
                if (tok.ttype != -2) {
                    throw new IllegalArgumentException("Parse error");
                }
                final int n1 = (int)tok.nval;
                if (tok.nextToken() != 47) {
                    throw new IllegalArgumentException("Parse error");
                }
                final int n2 = this.getInt(tok);
                this.faceno.add(n1);
                this.faceno.add(n2);
            }
        }
        
        private void setGroup(final int gr, final int sub) {
            this.groups.add(gr);
            this.groups.add(sub);
            this.groups.add(this.fno);
            if (this.grcnt.size() < gr) {
                throw new IllegalArgumentException("Parse error");
            }
            if (this.grcnt.size() == gr) {
                this.grcnt.add(0);
            }
            this.grcnt.set(gr, Math.max(this.grcnt.get(gr), sub + 1));
        }
        
        private void parseGroup(final StreamTokenizer tok) throws IOException {
            final int n1 = this.getInt(tok);
            int n2 = 0;
            tok.nextToken();
            if (tok.ttype == 95) {
                n2 = this.getInt(tok);
                tok.nextToken();
            }
            this.setGroup(n1, n2);
            if (tok.ttype != 10) {
                throw new IllegalArgumentException("Parse error");
            }
        }
        
        public void readModel(final InputStream fis) throws IOException {
            final BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            final StreamTokenizer tok = new StreamTokenizer(r);
            tok.commentChar(35);
            tok.eolIsSignificant(true);
            tok.lowerCaseMode(false);
            tok.parseNumbers();
            tok.quoteChar(34);
            tok.ordinaryChar(47);
            while (tok.nextToken() != -1) {
                if (tok.ttype != 10) {
                    if (tok.ttype != -3) {
                        throw new IllegalArgumentException("Parse error");
                    }
                    final String sval = tok.sval;
                    switch (sval) {
                        case "v": {
                            final Vector3 f1 = new Vector3();
                            f1.x = this.getFloat(tok);
                            f1.y = this.getFloat(tok);
                            f1.z = this.getFloat(tok);
                            this.vertex.add(f1);
                            this.endLine(tok);
                            continue;
                        }
                        case "vt": {
                            final double f2 = this.getFloat(tok);
                            final double f3 = this.getFloat(tok);
                            this.texvert.add(new TexVertex(0, f2, f3));
                            this.endLine(tok);
                            continue;
                        }
                        case "vtc": {
                            final double f2 = this.getFloat(tok);
                            final double f3 = this.getFloat(tok);
                            final TexVertex tv = new TexVertex(0, f2, f3);
                            tv.r = (float)this.getFloat(tok);
                            tv.g = (float)this.getFloat(tok);
                            tv.b = (float)this.getFloat(tok);
                            this.texvert.add(tv);
                            this.endLine(tok);
                            continue;
                        }
                        case "f": {
                            this.parseFace(tok);
                            continue;
                        }
                        case "g": {
                            this.parseGroup(tok);
                            continue;
                        }
                        default: {
                            this.eatLine(tok);
                            continue;
                        }
                    }
                }
            }
            fis.close();
        }
    }
}
